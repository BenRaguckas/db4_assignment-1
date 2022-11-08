USE A00276965;
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