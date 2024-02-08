--Show the first superhero
SELECT
    *
FROM
    superhero
ORDER BY
    id
LIMIT
    1;

--Show the last superhero
SELECT
    *
FROM
    superhero
ORDER BY
    id DESC
LIMIT
    1;

--Show the superhero name of all superheroes with the same colour eyes and hair
SELECT
    superhero_name
FROM
    superhero
WHERE
    eye_colour_id = hair_colour_id;

--Show the superhero name and full name of all superheroes with a superhero name that ends in Man or Woman
SELECT
    superhero_name,
    full_name
FROM
    superhero
WHERE
    superhero_name LIKE '%man'
    --Show the superhero name of the 5 tallest superheroes in descending order
SELECT
    superhero_name,
    height_cm
FROM
    superhero
WHERE
    height_cm IS NOT null
ORDER BY
    height_cm DESC
LIMIT
    5;

--- Show the superhero name of the 5 lightest superheroes with weight greater than 0kg in ascending order
SELECT
    superhero_name,
    weight_kg
FROM
    superhero
WHERE
    weight_kg > 0
ORDER BY
    weight_kg
LIMIT
    5;

--Show the first 5 superheroes who do not use an alias in ascending order of id
SELECT
    *
FROM
    superhero
WHERE
    full_name = superhero_name
ORDER BY
    id
LIMIT
    5;

-- Show the first 5 superheroes who do not have a full name in ascending order of id
SELECT
    *
FROM
    superhero
WHERE
    full_name IS null
ORDER BY
    id
LIMIT
    5;

--Show the superhero name of all superheroes who are neither Male or Female
SELECT
    *
FROM
    superhero
    JOIN gender ON superhero.gender_id = gender.id
WHERE
    gender <> 'Male'
    AND gender <> 'Female';

--Show the superhero names of all superheroes who are Female and Neutral in alignment
SELECT
    *
FROM
    superhero
    JOIN gender ON superhero.gender_id = gender.id
    JOIN alignment ON superhero.alignment_id = alignment.id
WHERE
    gender.gender = 'Female'
    AND alignment.alignment = 'Neutral';

-- Show the number of superheroes who have superhero names that start with the letter A
SELECT
    count(*) AS total
FROM
    superhero
WHERE
    superhero_name LIKE 'A%';

-- Show the superhero name of the tallest superheroes for each gender
SELECT
    MAX(superhero_name) AS tallest_hero,
    gender_id,
    MAX(height_cm) AS height
FROM
    superhero
GROUP BY
    gender_id;

-- Show the distribution of superheroes by publisher. Exclude superheroes who do not belong to any publishers (publisher is `‘’`)
SELECT
    publisher_name,
    COUNT(*)
FROM
    superhero
    JOIN publisher ON superhero.publisher_id = publisher.id
WHERE
    publisher_name <> ''
GROUP BY
    publisher_name;

--Show the superhero names of all superheroes who take on multiple forms (their name appears multiple times in the database)
? ? ? ?
--- Show the distribution of superheroes by eye colour and hair colour. Order your result, showing the 10 most common combinations
SELECT
    eyes.colour as eye_color,
    hair.colour as hair_colour,
    COUNT(*) as "count"
FROM
    superhero hero
    JOIN colour eyes ON hero.eye_colour_id = eyes.id
    JOIN colour hair ON hero.hair_colour_id = hair.id
GROUP BY
    eyes.colour,
    hair.colour
ORDER BY
    "count" DESC
LIMIT
    10;

--Show the number of superheroes who have the exact same eye and hair colour. Exclude superheroes who do not have any colour in their eyes or hair (colour is No Colour).
SELECT
    eyes.colour as eye_color,
    hair.colour as hair_colour,
    COUNT(*) as "count"
FROM
    superhero hero
    JOIN colour eyes ON hero.eye_colour_id = eyes.id
    JOIN colour hair ON hero.hair_colour_id = hair.id
WHERE
    eye_colour_id = hair_colour_id
    AND eyes.colour <> 'No Colour'
    AND hair.colour <> 'No Colour'
GROUP BY
    eyes.colour,
    hair.colour
ORDER BY
    "count" DESC
LIMIT
    10;

--- Show the average height and average weight of all superheroes whose name ends in `Man`
SELECT
    AVG(height_cm) AS "Average Height",
    AVG(weight_kg) AS "Average Weight"
FROM
    superhero
WHERE
    LOWER(superhero_name) LIKE '%man'
    --Show the top 10 races by average height having average height less than 200cm. Display your results in descending order
SELECT
    race,
    AVG(height_cm) AS "Average Height"
FROM
    superhero
    JOIN race ON race_id = race.id
WHERE
    height_cm < 200
GROUP BY
    race
ORDER BY
    "Average Height" DESC
LIMIT
    10;

--Show the gender distribution by count, average height, and average weight of all superheroes without a publisher (publisher name is ‘’)
SELECT
    gender,
    COUNT(gender) AS "Gender Distribution",
    AVG(height_cm) AS "Avg Height",
    AVG(weight_kg) AS "Avg Height"
FROM
    superhero
    JOIN gender ON gender_id = gender.id
    JOIN publisher ON publisher_id = publisher.id
WHERE
    publisher_name = ''
GROUP BY
    gender;

--Show the 5 most common races of superheroes who are not Good. Display your results in descending order and exclude superheroes without a race (race is -)
SELECT
    race,
    COUNT(alignment) AS Alignment_Count
FROM
    superhero
    JOIN alignment ON alignment_id = alignment.id
    JOIN race ON race_id = race.id
WHERE
    alignment <> 'Good'
    AND race <> '-'
GROUP BY
    race
ORDER BY
    Alignment_Count DESC
LIMIT
    5;

--Show the names of all superheroes as their first name, superhero name in quotes, last name. For example, 
--Charles Chandler is known as 3-D Man so he will be displayed as Charles “3-D Man” Chandler. Display only 
--superheroes from Marvel Comics whose full names consist of only their first and last name (exclude 
--superheroes such as Bob and Angel Salvadore Bohusk)
SELECT
    SUBSTRING(full_name, 1, POSITION(' ' IN full_name)) || ' "' || superhero_name || '" ' || SUBSTRING(
        full_name,
        POSITION(' ' IN full_name) + 1,
        LENGTH (full_name) - POSITION(' ' IN full_name)
    ) AS formatted_name
FROM
    superhero
    JOIN publisher ON publisher_id = publisher.id
WHERE
    publisher_name = 'Marvel Comics'
    AND CHAR_LENGTH(full_name) - CHAR_LENGTH(REPLACE (full_name, ' ', '')) = 1;

--Show the superhero name, eye colour, hair colour, and skin colour of all superheroes with either Blue, Brown, or Green eyes. Display the first 20 results
SELECT
    superhero_name,
    eye.colour AS eye_colour,
    hair.colour AS hair_colour,
    skin.colour AS skin_colour
FROM
    superhero hero
    JOIN colour eye ON eye_colour_id = eye.id
    JOIN colour hair ON hair_colour_id = hair.id
    JOIN colour skin ON skin_colour_id = skin.id
WHERE
    eye.colour = 'Blue'
    OR eye.colour = 'Brown'
    OR eye.colour = 'Green'
LIMIT
    20;

--Show the superhero name and full name of the 10th superhero (by id) who uses an alias
SELECT
    id,
    superhero_name,
    full_name
FROM
    superhero
WHERE
    full_name IS NOT null
    AND full_name <> '-'
ORDER BY
    id
LIMIT
    1
OFFSET
    9;

--
SELECT
    gender,
    COUNT(*) AS total_heroes
FROM
    superhero
    JOIN gender ON gender_id = gender.id
    JOIN alignment align ON alignment_id = align.id
    JOIN colour eye ON eye_colour_id = eye.id
    JOIN colour hair ON hair_colour_id = hair.id
WHERE
    (
        alignment = 'Good'
        AND eye.colour = 'Green'
    )
    OR hair.colour LIKE '%Red%'
    OR (
        eye.colour LIKE '%White%'
        AND hair.colour <> 'Black'
    )
GROUP BY
    gender
ORDER BY
    gender;

--
SELECT
    superhero_name,
    height_cm
FROM
    (
        SELECT
            superhero_name,
            height_cm,
            race_id,
            AVG(height_cm) OVER (
                PARTITION BY
                    race_id
            ) AS avg_height_by_race
        FROM
            superhero
    ) AS Subquery
WHERE
    ABS(height_cm - avg_height_by_race) <= 10;

--
SELECT
    publisher_name,
    COUNT(*) AS total_superheroes,
    COUNT(
        CASE
            WHEN gender.gender = 'Female' THEN 1
        END
    ) AS female_superheroes,
    (
        COUNT(
            CASE
                WHEN gender.gender = 'Female' THEN 1
            END
        ) * 100.0 / COUNT(*)
    ) AS percentage_female
FROM
    superhero
    JOIN publisher ON superhero.publisher_id = publisher.id
    JOIN gender ON superhero.gender_id = gender.id
WHERE
    gender.gender IS NOT NULL
GROUP BY
    publisher_name
ORDER BY
    percentage_female DESC
LIMIT
    1;

--
WITH
    AlignmentCounts AS (
        SELECT
            publisher_name,
            alignment.alignment,
            COUNT(*) AS alignment_count
        FROM
            superhero
            LEFT JOIN publisher ON superhero.publisher_id = publisher.id
            JOIN alignment ON superhero.alignment_id = alignment.id
        GROUP BY
            publisher_name,
            alignment.alignment
    )
SELECT
    publisher_name,
    alignment,
    ROUND(
        (
            alignment_count * 100.0 / SUM(alignment_count) OVER (
                PARTITION BY
                    publisher_name
            )
        ),
        0
    ) AS percentage
FROM
    AlignmentCounts
ORDER BY
    publisher_name,
    alignment;

--
SELECT
    publisher_name,
    COUNT(*) AS superhero_count,
    ROUND((COUNT(*) * 100.0 / total_count), 0) AS percentage
FROM
    superhero
    JOIN publisher ON superhero.publisher_id = publisher.id
    CROSS JOIN (
        SELECT
            COUNT(*) AS total_count
        FROM
            superhero
    ) AS TotalCount
WHERE
    publisher_name IS NOT NULL
    AND publisher_name <> ''
GROUP BY
    publisher_name,
    total_count
HAVING
    COUNT(*) * 100.0 / total_count >= 1
ORDER BY
    percentage DESC;

--
WITH
    SuperheroStats AS (
        SELECT
            superhero_name,
            race,
            height_cm,
            AVG(height_cm) OVER (
                PARTITION BY
                    race
            ) AS avg_height_race,
            ABS(
                height_cm - AVG(height_cm) OVER (
                    PARTITION BY
                        race
                )
            ) AS height_diff
        FROM
            superhero
            JOIN race ON race_id = race.id
        WHERE
            weight_kg < 80
            AND height_cm > 0
            AND race <> '-'
    )
SELECT
    superhero_name,
    race,
    height_diff
FROM
    SuperheroStats
ORDER BY
    height_diff DESC
LIMIT
    10;