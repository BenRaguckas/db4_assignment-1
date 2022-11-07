USE A00276965;

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
