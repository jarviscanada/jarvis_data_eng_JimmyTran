-- 1. switch to `host_agent`
--\c host_agent
-- 2. create `cd.members` table if not exist
CREATE TABLE
    IF NOT EXISTS cd.members (
        memid integer NOT NULL,
        surname character varying(200) NOT NULL,
        firstname character varying(200) NOT NULL,
        address character varying(300) NOT NULL,
        zipcode integer NOT NULL,
        telephone character varying(20) NOT NULL,
        recommendedby integer,
        joindate timestamp NOT NULL,
        CONSTRAINT members_pk PRIMARY KEY (memid),
        CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members (memid) ON DELETE SET NULL
    );

-- 2. create `cd.facilities` table if not exist
CREATE TABLE
    IF NOT EXISTS cd.facilities (
        facid integer NOT NULL,
        name character varying(100) NOT NULL,
        membercost numeric NOT NULL,
        guestcost numeric NOT NULL,
        initialoutlay numeric NOT NULL,
        monthlymaintenance numeric NOT NULL,
        CONSTRAINT facilities_pk PRIMARY KEY (facid)
    );

-- 2. create `cd.bookings` table if not exist
CREATE TABLE
    IF NOT EXISTS cd.bookings (
        bookid integer NOT NULL,
        facid integer NOT NULL,
        memid integer NOT NULL,
        starttime timestamp NOT NULL,
        slots integer NOT NULL,
        CONSTRAINT bookings_pk PRIMARY KEY (bookid),
        CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities (facid),
        CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members (memid)
    );