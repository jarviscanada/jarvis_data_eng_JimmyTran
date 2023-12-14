--1
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
    --2
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

--3
UPDATE cd.facilities
SET
    initialoutlay = 10000
WHERE
    facid = 1;

--4
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

--5
delete FROM cd.bookings;

--6
DELETE FROM cd.members
WHERE
    memid = 37;

--7
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

--8
SELECT
    *
FROM
    cd.facilities
WHERE
    name LIKE '%Tennis%';

--9
SELECT
    *
FROM
    cd.facilities
WHERE
    facid IN (1, 5);

--10
SELECT
    memid,
    surname,
    firstname,
    joindate
FROM
    cd.members
WHERE
    joindate >= '2012-09-01';

--11
SELECT
    surname
FROM
    cd.members
UNION
SELECT
    name
FROM
    cd.facilities
    --12
SELECT
    starttime
FROM
    cd.bookings
    INNER JOIN cd.members ON cd.members.memid = cd.bookings.memid
WHERE
    cd.members.firstname = 'David'
    AND cd.members.surname = 'Farrell';

--13
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
    starttime;

--14
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

--15
SELECT DISTINCT
    rec.firstname,
    rec.surname
FROM
    cd.members mems
    INNER JOIN cd.members rec ON rec.memid = mems.recommendedby
ORDER BY
    surname,
    firstname
    --16
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

--17
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

--18
SELECT
    facid,
    SUM(slots) AS "Total Slots"
FROM
    cd.bookings
GROUP BY
    facid
ORDER BY
    facid;

--19
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

--20
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

--21
SELECT
    count(distinct memid)
FROM
    cd.bookings
    --22
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

--23
SELECT
    COUNT(*) OVER (),
    firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate
    --24
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
    --25
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
    --26
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

--28
SELECT
    substr (mems.surname, 1, 1) AS letter,
    count(*) AS count
FROM
    cd.members mems
GROUP BY
    letter
ORDER BY
    letter