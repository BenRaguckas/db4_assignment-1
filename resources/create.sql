DROP DATABASE IF EXISTS A00276965;
CREATE DATABASE A00276965;
USE A00276965;
DROP TABLE IF EXISTS android_apps;
CREATE TABLE android_apps(
   id             INTEGER        NOT NULL PRIMARY KEY,
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