-- Create table--
-- Created by Jin LIu, 5/5/2020--
DROP TABLE COURSE;
DROP TABLE HelpQueue;
DROP TABLE Event;
--helpQueue table--
create table HelpQueue(
    workStationName         VARCHAR(30),
    requestTime             TIMESTAMP(6),
    cancelTime              TIMESTAMP(6),
    status                  varchar(10),
    waitTime                INTERVAL DAY(1) TO SECOND(2),
    PRIMARY KEY(workStationName)
);


--event table--
create table Event(
    ID                  VARCHAR(30) NOT Null,
    eventType           VARCHAR(20),
    workStationName     VARCHAR(30),
    originator          VARCHAR(20),
    courseNum           VARCHAR(20),
    sectionNum          VARCHAR(20),
    eventTime           TIMESTAMP(7),
    PRIMARY KEY(ID)
);

create table Course(
    ID                  VARCHAR(30) NOT NULL,
    courseNum           VARCHAR(30),
    section             VARCHAR(30),
    endDate             DATE,
    startDate           DATE,
    dayOfWeek           VARCHAR(100),
    startTime           DATE,
    endTime             DATE,
    PRIMARY KEY(ID)
);

