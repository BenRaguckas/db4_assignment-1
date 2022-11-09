USE A00276965;

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
