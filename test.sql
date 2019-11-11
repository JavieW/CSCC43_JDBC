CREATE TABLE country(
    cid         INTEGER     PRIMARY KEY,
    cname       VARCHAR     NOT NULL
    );

CREATE TABLE player(
    pid         INTEGER     PRIMARY KEY,
    pname       VARCHAR     NOT NULL,
    globalrank  INTEGER     NOT NULL,
    cid         INTEGER     REFERENCES country(cid) ON DELETE RESTRICT
    );

CREATE TABLE record(
    pid         INTEGER     REFERENCES player(pid) ON DELETE RESTRICT,
    year        INTEGER     NOT NULL,
    wins        INTEGER     NOT NULL,
    losses      INTEGER     NOT NULL,
    PRIMARY KEY(pid, year));

CREATE TABLE tournament(
    tid         INTEGER     PRIMARY KEY,
    tname       VARCHAR     NOT NULL,
    cid         INTEGER     REFERENCES country(cid) ON DELETE RESTRICT 
    );

CREATE TABLE court(
    courtid     INTEGER     PRIMARY KEY,
    courtname   VARCHAR     NOT NULL,
    capacity    INTEGER     NOT NULL,
    tid         INTEGER     REFERENCES tournament(tid) ON DELETE RESTRICT
    );

CREATE TABLE champion(
    pid     INTEGER     REFERENCES player(pid) ON DELETE RESTRICT,
    year    INTEGER     NOT NULL, 
    tid     INTEGER     REFERENCES tournament(tid) ON DELETE RESTRICT,
    PRIMARY KEY(tid, year));

CREATE TABLE event(
    eid        INTEGER     PRIMARY KEY,
    year       INTEGER     NOT NULL,
    courtid    INTEGER     REFERENCES court(courtid) ON DELETE RESTRICT,
    winid      INTEGER     REFERENCES player(pid) ON DELETE RESTRICT,
    lossid     INTEGER     REFERENCES player(pid) ON DELETE RESTRICT,
    duration   INTEGER     NOT NULL
    );


insert into Player values
(1, 'Cole', 365, 5), 
(2, 'Avery', 585, 5), 
(3, 'Sam', 502, 12), 
(4, 'Madeleine', 511, 18), 
(5, 'Cole', 450, 5), 
(6, 'Michael', 1000, 12), 
(7, 'Mackenzie', 700, 5), 
(8, 'Mackenzie', 701, 5),
(9, 'Micah', 498, 4), 
(10, 'Jiaqi', 509, 4), 
(11, 'Jamieson', 502, 6);
