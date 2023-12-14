# Introduction

This project emulates a sports club's booking database, which consists of 3 tables; `cd.members`, `cd.bookings`, and `cd.facilities`. The tables are hosted on a Docker/PSQL server locally and PGADMIN 4 was used to run queries. The purpose of the project was to familiarize ourselves with SQL and learn in depth about how common functions/techniques such as performing CRUD operations on single tables as well as multiple tables using JOINs and subqueries. Aggregates as window functions were also practiced during the exercise as well as string functions.

# SQL Queries

###### Table Setup (DDL)

The following DDL was used to create the tables for the exercise.

```sql
CREATE TABLE cd.members (
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
```

```sql
CREATE TABLE cd.facilities (
        facid integer NOT NULL,
        name character varying(100) NOT NULL,
        membercost numeric NOT NULL,
        guestcost numeric NOT NULL,
        initialoutlay numeric NOT NULL,
        monthlymaintenance numeric NOT NULL,
        CONSTRAINT facilities_pk PRIMARY KEY (facid)
    );
```

```sql
CREATE TABLE cd.bookings (
        bookid integer NOT NULL,
        facid integer NOT NULL,
        memid integer NOT NULL,
        starttime timestamp NOT NULL,
        slots integer NOT NULL,
        CONSTRAINT bookings_pk PRIMARY KEY (bookid),
        CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities (facid),
        CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members (memid)
    );
```

The following command was used to run the script clubdata.sql which created the tables and inserted the dummy data into the database.

`psql -U <username> -f clubdata.sql -d postgres -x -q`

### Verify the database was set up correctly

###### Question 0: Show all members

```sql
SELECT *
FROM cd.members
```

### Modifying Data

###### Question 1: Insert some data into a table

```sql
INSERT INTO
    cd.facilities (
        facid,
        name,
        membercost,
        guestcost,
        initialoutlay,
        monthlymaintenance
    )
VALUES
    (9, 'Spa', 20, 30, 100000, 800)
```

###### Question 2: Insert calculated data into a table

```sql
INSERT into
    cd.facilities (
        facid,
        name,
        membercost,
        guestcost,
        initialoutlay,
        monthlymaintenance
    )
select
    (
        select
            max(facid)
        from
            cd.facilities
    ) + 1,
    'Spa',
    20,
    30,
    100000,
    800;
```

###### Question 3: Update some existing data

```sql
UPDATE cd.facilities
SET
    initialoutlay = 10000
WHERE
    facid = 1;
```

###### Question 4: Update a row based on the contents of another row

```sql
UPDATE cd.facilities
SET
    membercost = (
        select
            membercost * 1.1
        from
            cd.facilities
        where
            facid = 0
    ),
    guestcost = (
        select
            guestcost * 1.1
        from
            cd.facilities
        where
            facid = 0
    )
WHERE
    facid = 1;
```

###### Question 5: Delete all bookings

```sql
delete FROM cd.bookings;
```

###### Question 6: Delete a member from the cd.members table

```sql
DELETE FROM cd.members
WHERE
    memid = 37;
```

### Basics

###### Question 7: Control which rows are retrieved - part 2

```sql
SELECT
    facid,
    name,
    membercost,
    monthlymaintenance
FROM
    cd.facilities
WHERE
    membercost < (monthlymaintenance / 50)
    AND membercost > 0;
```

###### Question 8: Basic string searches

```sql
SELECT
    *
FROM
    cd.facilities
WHERE
    name LIKE '%Tennis%';
```

###### Question 9: Matching against multiple possible values

```sql
SELECT
    *
FROM
    cd.facilities
WHERE
    facid IN (1, 5);
```

###### Question 10: Working with dates

```sql
SELECT
    memid,
    surname,
    firstname,
    joindate
FROM
    cd.members
WHERE
    joindate >= '2012-09-01';
```

###### Question 11: Combining results from multiple queries

```sql
SELECT
    surname
FROM
    cd.members
UNION
SELECT
    name
FROM
    cd.facilities
```

### Join

###### Question 12: Retrieve the start times of members' bookings

```sql
SELECT
    starttime
FROM
    cd.bookings
    INNER JOIN cd.members ON cd.members.memid = cd.bookings.memid
WHERE
    cd.members.firstname = 'David'
    AND cd.members.surname = 'Farrell';
```

###### Question 13: Work out the start times of bookings for tennis courts

```sql
SELECT
    starttime AS start,
    name
FROM
    cd.bookings
    INNER JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid
WHERE
    name in ('Tennis Court 2', 'Tennis Court 1')
    and starttime >= '2012-09-21'
    and starttime < '2012-09-22'
ORDER BY
```

###### Question 14: Produce a list of all members, along with their recommender

```sql
SELECT
    mems.firstname AS memfname,
    mems.surname AS memsname,
    rec.firstname AS recfname,
    rec.surName AS recsname
FROM
    cd.members mems
    LEFT JOIN cd.members rec ON rec.memid = mems.recommendedby
ORDER BY
    memsname,
    memfname;
```

###### Question 15: Produce a list of all members who have recommended another member

```sql
SELECT DISTINCT
    rec.firstname,
    rec.surname
FROM
    cd.members mems
    INNER JOIN cd.members rec ON rec.memid = mems.recommendedby
ORDER BY
    surname,
    firstname
```

###### Question 16: Produce a list of all members, along with their recommender, using no joins

```sql
SELECT DISTINCT
    mems.firstname || ' ' || mems.surname as member,
    (
        SELECT
            recs.firstname || ' ' || recs.surname as recommender
        FROM
            cd.members recs
        Where
            recs.memid = mems.recommendedby
    )
FROM
    cd.members mems
ORDER BY
    member;
```

### Aggregation

###### Question 17: Count the number of recommendations each member makes

```sql
SELECT
    recommendedby,
    COUNT(*)
FROM
    cd.members
WHERE
    recommendedby IS NOT NULL
GROUP BY
    recommendedby
ORDER BY
    recommendedby;
```

###### Question 18: List the total slots booked per facility

```sql
SELECT
    facid,
    SUM(slots) AS "Total Slots"
FROM
    cd.bookings
GROUP BY
    facid
ORDER BY
    facid;
```

###### Question 19: List the total slots booked per facility in a given month

```sql
SELECT
    facid,
    SUM(slots) AS "Total Slots"
FROM
    cd.bookings
WHERE
    starttime >= '2012-09-01'
    AND starttime < '2012-10-01'
GROUP BY
    facid
ORDER BY
    SUM(slots);

```

###### Question 20: List the total slots booked per facility per month

```sql
SELECT
    facid,
    extract(
        month
        FROM
            starttime
    ) AS month,
    SUM(slots) AS "Total Slots"
FROM
    cd.bookings
WHERE
    extract(
        year
        FROM
            starttime
    ) = 2012
GROUP BY
    facid,
    month
ORDER BY
    facid,
    month;
```

###### Question 21: Find the count of members who have made at least one booking

```sql
SELECT
    count(distinct memid)
FROM
    cd.bookings
```

###### Question 22: List each member's first booking after September 1st 2012

```sql
SELECT
    mems.surname,
    mems.firstname,
    mems.memid,
    min(bks.starttime) AS starttime
FROM
    cd.bookings bks
    INNER JOIN cd.members mems ON mems.memid = bks.memid
WHERE
    starttime >= '2012-09-01'
GROUP BY
    mems.surname,
    mems.firstname,
    mems.memid
ORDER BY
    mems.memid;
```

###### Question 23: Produce a list of member names, with each row containing the total member count

```sql
SELECT
    COUNT(*) OVER (),
    firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate
```

###### Question 24: Produce a numbered list of members

```sql
SELECT
    row_number() OVER (
        ORDER BY
            joindate
    ),
    firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate
```

###### Question 25: Output the facility id that has the highest number of slots booked, again

```sql
SELECT
    facid,
    total
FROM
    (
        SELECT
            facid,
            sum(slots) total,
            rank() OVER (
                ORDER BY
                    sum(slots) DESC
            ) rank
        FROM
            cd.bookings
        GROUP BY
            facid
    ) AS ranked
WHERE
    rank = 1
```

### String

###### Question 26: Format the names of members

```sql

```

###### Question 27: Find telephone numbers with parentheses

```sql
SELECT
    surname || ', ' || firstname AS NAME
FROM
    cd.members
    --27
SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone similar to '%[()]%';
```

###### Question 28: Count the number of members whose surname starts with each letter of the alphabet

```sql
SELECT
    substr (mems.surname, 1, 1) AS letter,
    count(*) AS count
FROM
    cd.members mems
GROUP BY
    letter
ORDER BY
    letter
```
