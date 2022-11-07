DROP VIEW IF EXISTS apps_list;

DELIMITER //
CREATE VIEW apps_list as SELECT
	id as "ID", app as "App Name", category as "Category", rating as "Rating", reviews as "Reviews", size as "Size",
    installs as "Installs", type as "Type", price as "Price", content_rating as "Content rating", genres as "Genre(s)",
    last_updated as "Last updated", current_ver as "App version", android_ver as "Android version"
    FROM android_apps;
END//
