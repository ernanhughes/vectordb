-- Role: vectordb
DROP ROLE IF EXISTS vectordb;
CREATE ROLE vectordb WITH
  LOGIN
  SUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  REPLICATION
  ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:D4zJTiB7OssEJ0gUGBOXiA==$OW9YecRm0XYr/KXNtce7ESbfEWkUkWtxdZnEP99pUf0=:KeHjKgSFZFjdABuTzcEkSAZCjD0PLty1KmsLncMU2Uo=';
