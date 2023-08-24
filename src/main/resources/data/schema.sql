DROP TABLE IF EXISTS todo;
CREATE TABLE todo (
    id SERIAL,
	title TEXT NOT NULL,
	status TEXT NOT NULL DEFAULT 'New',
	description TEXT NULL,
	completed INTEGER NOT NULL DEFAULT 0,
	"start" timestamp,
	"end" timestamp,
	schedule TEXT,
	username TEXT,
	important INTEGER NOT NULL DEFAULT 0,
	created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS todo_history;
CREATE TABLE todo_history (
    id SERIAL,
    todo_id INTEGER,
	title TEXT NOT NULL,
	status TEXT NOT NULL,
	description TEXT,
	completed INTEGER,
	"start" timestamp,
	"end" timestamp,
	schedule TEXT,
	username TEXT,
	important INTEGER,
	created timestamp,
	updated timestamp
);

CREATE INDEX index_todo_title ON todo(title);

DROP TABLE IF EXISTS setting;
CREATE TABLE setting (
    id SERIAL,
	"section" TEXT NOT NULL,
	name TEXT NOT NULL,
	"value" TEXT NOT NULL,
	description TEXT,
	seq INTEGER,
	username TEXT,
	created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS setting_history;
CREATE TABLE setting_history (
    id SERIAL,
    setting_id INTEGER,
	"section" TEXT NOT NULL,
	name TEXT NOT NULL,
	"value" TEXT NOT NULL,
	description TEXT,
	seq INTEGER,
	username TEXT,
	created timestamp,
	updated timestamp
);

DROP TABLE IF EXISTS attribute;
CREATE TABLE attribute (
    id SERIAL,
	objectId INTEGER NOT NULL,
	objectType TEXT NOT NULL,
	name TEXT NOT NULL,
	"value" TEXT NOT NULL,
	description TEXT,
	seq INTEGER,
	username TEXT,
	created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS attribute_history;
CREATE TABLE attribute_history (
    id SERIAL,
    attribute_id INTEGER,
	"section" TEXT NOT NULL,
	name TEXT NOT NULL,
	"value" TEXT NOT NULL,
	description TEXT,
	seq INTEGER,
	username TEXT,
	created timestamp,
	updated timestamp
);


DROP TABLE IF EXISTS session;
CREATE TABLE session (
    id SERIAL,
	objectId INTEGER NOT NULL,
	objectType TEXT NOT NULL,
	name TEXT NOT NULL,
	"value" TEXT NOT NULL,
	description TEXT,
	source TEXT,
	username TEXT,
	expiry TEXT,
	created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS session_history;
CREATE TABLE session_history (
    id SERIAL,
    session_id INTEGER,
	"section" TEXT NOT NULL,
	name TEXT NOT NULL,
	"value" TEXT NOT NULL,
	description TEXT,
	source TEXT,
	username TEXT,
	expiry TEXT,
	created timestamp,
	updated timestamp
);

DROP TABLE IF EXISTS list;
CREATE TABLE list (
    id SERIAL,
	name TEXT NOT NULL,
	username TEXT,
	created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS list_history;
CREATE TABLE list_history (
    id SERIAL,
    list_id INTEGER,
	name TEXT NOT NULL,
	username TEXT,
	created timestamp,
	updated timestamp
);

DROP TABLE IF EXISTS list_item;
CREATE TABLE list_item (
    id SERIAL,
	list_id INTEGER NOT NULL,
	todo_id INTEGER NOT NULL,
	username TEXT,
	created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS list_item_history;
CREATE TABLE list_item_history (
    id SERIAL,
    list_item_id INTEGER,
	list_id INTEGER NOT NULL,
	todo_id INTEGER NOT NULL,
	username TEXT,
	created timestamp,
	updated timestamp
);

DROP TABLE IF EXISTS uploaded_file;
CREATE TABLE uploaded_file (
	id SERIAL,
	name TEXT NOT NULL,
	uploaded_path TEXT,
	file_size INTEGER,
	uploaded_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
	user_name TEXT,
	file_type TEXT NOT NULL,
	data BYTEA
);

DROP TABLE IF EXISTS event;
CREATE TABLE event (
	id SERIAL,
	source TEXT NOT NULL,
	data TEXT NOT NULL,
	username TEXT,
	created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS error_log;
CREATE TABLE error_log (
	rowid SERIAL,
    message TEXT,
    type TEXT,
    fileName TEXT,
	username TEXT NULL,
	created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
