DROP DATABASE IF EXISTS A00276965;
CREATE DATABASE A00276965;
USE A00276965;
DROP TABLE IF EXISTS android_apps;
CREATE TABLE android_apps(
   id             INTEGER        NOT NULL PRIMARY KEY AUTO_INCREMENT,
   app            VARCHAR(256)   NOT NULL,
   category       VARCHAR(64)    NOT NULL,
   rating         DECIMAL(7,1),
   reviews        INTEGER        NOT NULL,
   size           DECIMAL(7,1),
   installs       VARCHAR(32)    NOT NULL,
   type           VARCHAR(32)    NOT NULL,
   price          VARCHAR(32)    NOT NULL,
   content_rating VARCHAR(32)    NOT NULL,
   genres         VARCHAR(256)   NOT NULL,
   last_updated   DATE           NOT NULL,
   current_ver    VARCHAR(64),
   android_ver    VARCHAR(64)
);

DROP TABLE IF EXISTS android_apps_bin;
CREATE TABLE android_apps_bin(
   entry          INTEGER        NOT NULL PRIMARY KEY AUTO_INCREMENT,
   deleted_on     DATE           NOT NULL,
   id             INTEGER        NOT NULL,
   app            VARCHAR(256)   NOT NULL,
   category       VARCHAR(64)    NOT NULL,
   rating         DECIMAL(7,1),
   reviews        INTEGER        NOT NULL,
   size           DECIMAL(7,1),
   installs       VARCHAR(32)    NOT NULL,
   type           VARCHAR(32)    NOT NULL,
   price          VARCHAR(32)    NOT NULL,
   content_rating VARCHAR(32)    NOT NULL,
   genres         VARCHAR(256)   NOT NULL,
   last_updated   DATE           NOT NULL,
   current_ver    VARCHAR(64),
   android_ver    VARCHAR(64)
);

DROP TRIGGER IF EXISTS backup_delete;
DELIMITER //
CREATE TRIGGER backup_delete AFTER DELETE ON android_apps
FOR EACH ROW
BEGIN
	INSERT INTO android_apps_bin VALUES
		(0, NOW(), OLD.id, OLD.app, OLD.category, OLD.rating, OLD.reviews, OLD.size, OLD.installs, OLD.type, OLD.price, OLD.content_rating, OLD.genres, OLD.last_updated, OLD.current_ver, OLD.android_ver);
END//

DROP VIEW IF EXISTS apps_list;
CREATE VIEW apps_list as
    SELECT id as "ID", app as "App Name", category as "Category", rating as "Rating", reviews as "Reviews", size as "Size",
    installs as "Installs", type as "Type", price as "Price", content_rating as "Content rating", genres as "Genre(s)",
    last_updated as "Last updated", current_ver as "App version", android_ver as "Android version" FROM android_apps;

DROP VIEW IF EXISTS genres_list;
CREATE VIEW genres_list AS SELECT
	DISTINCT(SUBSTRING_INDEX(SUBSTRING_INDEX(genres, ';', n.digit+1), ";", -1)) genres FROM android_apps
	INNER JOIN (SELECT 0 digit UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3  UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) n
    ON LENGTH(REPLACE(genres, ';' , '')) <= LENGTH(genres)-n.digit;

DROP VIEW IF EXISTS category_list;
CREATE VIEW category_list AS SELECT category, COUNT(*) item_count FROM android_apps GROUP BY category;

DROP VIEW IF EXISTS content_list;
CREATE VIEW content_list AS SELECT content_rating, COUNT(*) item_count FROM android_apps GROUP BY content_rating;

DROP PROCEDURE IF EXISTS insert_item;

DELIMITER //
CREATE PROCEDURE insert_item (
	app			VARCHAR(256),
    category	VARCHAR(64),
    rating		DECIMAL(7,1),
    reviews		INTEGER,
    size		DECIMAL(7,1),
    installs	VARCHAR(32),
    type		VARCHAR(32),
    price		VARCHAR(32),
    content		VARCHAR(32),
    genres		VARCHAR(256),
    current_ver	VARCHAR(64),
    android_ver	VARCHAR(64),
    OUT out_ID  INTEGER
)
BEGIN
DECLARE sql_error TINYINT DEFAULT false;
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET sql_error = true;
START TRANSACTION;
	INSERT INTO android_apps(app,category,rating,reviews,size,installs,type,price,content_rating,genres,last_updated,current_ver,android_ver)
		VALUES (app, category, rating, reviews, size, installs, type, price, content, genres, NOW(), current_ver, android_ver);
    IF sql_error = FALSE THEN
		COMMIT;
		SELECT LAST_INSERT_ID() INTO out_ID;
	ELSE
		ROLLBACK;
	END IF;
END//

DROP PROCEDURE IF EXISTS update_item;

DELIMITER //
CREATE PROCEDURE update_item (
	input_ID			INTEGER,
	input_app			VARCHAR(256),
    input_category		VARCHAR(64),
    input_rating		DECIMAL(7,1),
    input_reviews		INTEGER,
    input_size			DECIMAL(7,1),
    input_installs		VARCHAR(32),
    input_type			VARCHAR(32),
    input_price			VARCHAR(32),
    input_content		VARCHAR(32),
    input_genres		VARCHAR(256),
    input_current_ver	VARCHAR(64),
    input_android_ver	VARCHAR(64)
)
BEGIN
DECLARE sql_error TINYINT DEFAULT false;
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET sql_error = true;
START TRANSACTION;
	UPDATE android_apps SET app=input_app, category=input_category, rating=input_rating, reviews=input_reviews, size=input_size, installs=input_installs, type=input_type,
		price=input_price, content_rating=input_content, genres=input_genres, last_updated=NOW(), current_ver=input_current_ver, android_ver=input_android_ver
		WHERE id = input_ID;
    IF sql_error = FALSE THEN
		COMMIT;
	ELSE
		ROLLBACK;
	END IF;
END//
