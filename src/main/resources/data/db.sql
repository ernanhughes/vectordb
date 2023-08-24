DROP DATABASE IF EXISTS vectordb;

CREATE DATABASE vectordb
    WITH
    OWNER = vectordb
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_Ireland.1252'
    LC_CTYPE = 'English_Ireland.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

