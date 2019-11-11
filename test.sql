DROP TABLE IF EXISTS champion CASCADE;
DROP TABLE IF EXISTS country CASCADE;
DROP TABLE IF EXISTS player CASCADE;
DROP TABLE IF EXISTS record CASCADE;
DROP TABLE IF EXISTS court CASCADE;
DROP TABLE IF EXISTS tournament CASCADE;
DROP TABLE IF EXISTS event CASCADE;

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
