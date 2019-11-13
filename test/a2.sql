SET search_path TO A2;

--If you define any views for a question (you are encouraged to), you must drop them
--after you have populated the answer table for that question.
--Good Luck!

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

DROP VIEW win2014;
DROP VIEW loss2014;
DROP VIEW wintime;
DROP VIEW losstime;