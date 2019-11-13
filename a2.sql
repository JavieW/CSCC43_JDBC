SET search_path TO A2;

--If you define any views for a question (you are encouraged to), you must drop them
--after you have populated the answer table for that question.
--Good Luck!

--Query 1
INSERT INTO query1(
    SELECT pname, cname, tname
    FROM player, country, champion, tournament
    WHERE player.pid = champion.pid AND champion.tid = tournament.tid AND tournament.cid = country.cid AND country.cid = player.cid
    ORDER BY pname
);


--Query 2
CREATE VIEW total AS 
    SELECT tid, SUM(capacity) AS totalCapacity
    FROM court
    GROUP BY tid;

INSERT INTO query2(
    SELECT tname, totalCapacity
    FROM total, tournament
    WHERE total.tid = tournament.tid AND totalCapacity >= ALL
    (
        SELECT totalCapacity
        FROM total
    )
    ORDER BY tname
);

DROP VIEW  total;


--Query 3
CREATE VIEW pair AS
    (
        SELECT winid AS pid, lossid AS opponent
        FROM event)
    UNION
    (
        SELECT lossid AS pid, winid AS opponent
        FROM event);

CREATE VIEW HighestRank AS
    SELECT pair.pid, min(globalRank) AS rank
    FROM pair, player 
    WHERE pair.opponent = player.pid
    GROUP BY pair.pid;

INSERT INTO query3(
    SELECT pair.pid AS p1id, p.pname AS p1name, pair.opponent AS p2id, o.pname AS p2name
    FROM pair, HighestRank, player p, player o
    WHERE pair.pid = p.pid AND pair.opponent = o.pid AND pair.pid = HighestRank.pid AND o.globalRank = HighestRank.rank
    ORDER BY p1name
);

DROP VIEW pair;
DROP VIEW HighestRank;


--Query 4
INSERT INTO query4(
    SELECT pid, pname
    FROM player
    WHERE  NOT EXISTS
    (
       (
           SELECT DISTINCT tid
           FROM tournament
       )
       EXCEPT 
       (
           SELECT DISTINCT tid
           FROM champion
           WHERE champion.pid = player.pid 
       )
    )
    ORDER BY pname
);


--Query 5
CREATE VIEW average AS
    SELECT pid, avg(wins) AS avgwins
    FROM record
    WHERE year >= 2011 AND year <= 2014
    GROUP BY pid 
    ORDER BY avgwins DESC;

INSERT INTO query5(
    SELECT average.pid, pname, avgwins
    FROM player, average
    WHERE player.pid = average.pid
    ORDER BY avgwins DESC
    LIMIT 10
);

DROP VIEW average;


--Query 6
CREATE VIEW win2011 AS
    SELECT pid, wins
    FROM record
    WHERE year = 2011;

CREATE VIEW win2012 AS
    SELECT pid, wins
    FROM record
    WHERE year = 2012;

CREATE VIEW win2013 AS
    SELECT pid, wins
    FROM record
    WHERE year = 2013;

CREATE VIEW win2014 AS
    SELECT pid, wins
    FROM record
    WHERE year = 2014;

INSERT INTO query6(
    SELECT player.pid, pname
    FROM player, win2011, win2012, win2013, win2014
    WHERE player.pid = win2011.pid AND player.pid = win2012.pid AND player.pid = win2013.pid AND player.pid = win2014.pid AND win2011.wins < win2012.wins AND win2012.wins < win2013.wins AND win2013.wins < win2014.wins
    ORDER BY pname
);

DROP VIEW win2011;
DROP VIEW win2012;
DROP VIEW win2013;
DROP VIEW win2014;


--Query 7
INSERT INTO query7(
    SELECT p1.pname, c1.year
    FROM champion c1, champion c2, player p1, player p2
    WHERE c1.pid = p1.pid AND c2.pid = p2.pid AND c1.pid = c2.pid AND c1.year = c2.year AND c1.tid < c2.tid
    ORDER BY pname DESC, year DESC
);


--Query 8
CREATE VIEW pair AS
    (SELECT winid AS p1id, lossid AS p2id
    FROM event)
    UNION
    (SELECT lossid AS p1id, winid AS p2id
    FROM event);

INSERT INTO query8(
    SELECT p1.pname AS p1name, p2.pname AS p2name, cname
    FROM pair, player p1, player p2, country
    WHERE pair.p1id = p1.pid AND pair.p2id = p2.pid AND p1.cid = p2.cid AND p1.cid = country.cid
    ORDER BY cname, p1name DESC
);

DROP VIEW pair;


--Query 9
CREATE VIEW cityCampion AS
    SELECT country.cid, count(*) AS champions
    FROM champion, player, country
    WHERE champion.pid = player.pid AND player.cid = country.cid
    GROUP BY country.cid;

INSERT INTO query9(
    SELECT cname, champions
    FROM cityCampion, country
    WHERE cityCampion.cid = country.cid AND champions >= ALL
    (
        SELECT champions
        FROM cityCampion
    )
    ORDER BY cname DESC
);

DROP VIEW cityCampion;


--Query 10
CREATE VIEW win2014 AS
    SELECT winid, count(*) AS wins
    FROM event
    WHERE year = 2014
    GROUP BY winid;

CREATE VIEW loss2014 AS
    SELECT lossid, count(*) AS losses
    FROM event
    WHERE year = 2014
    GROUP BY lossid;

CREATE VIEW wintime AS
    SELECT winid, SUM(duration) AS wintime, count(*) AS winnum
    FROM event
    GROUP BY winid;

CREATE VIEW losstime AS
    SELECT lossid, SUM(duration) AS losstime, count(*) AS lossnum
    FROM event
    GROUP BY lossid;

INSERT INTO query10(
    (SELECT pname
    FROM player, win2014, loss2014
    WHERE winid = lossid AND winid = player.pid AND wins > losses
    )
    INTERSECT
    (SELECT pname
    FROM player, wintime, losstime
    WHERE winid = lossid AND winid = player.pid AND (wintime + losstime)/(winnum + lossnum) > 200
    )
);
