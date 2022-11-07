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
    android_ver	VARCHAR(64)
)
BEGIN
DECLARE sql_error TINYINT DEFAULT false;
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET sql_error = true;
START TRANSACTION;
	INSERT INTO android_apps(app,category,rating,reviews,size,installs,type,price,content_rating,genres,last_updated,current_ver,android_ver)
		VALUES (app, category, rating, reviews, size, installs, type, price, content, genres, NOW(), current_ver, android_ver);
    IF sql_error = FALSE THEN
		COMMIT;
	ELSE
		ROLLBACK;
	END IF;
END//
