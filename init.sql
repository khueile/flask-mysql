CREATE DATABASE IF NOT EXISTS student_database;
USE student_database;
CREATE TABLE IF NOT EXISTS students (
    name VARCHAR(255),
    addr VARCHAR(255),
    city VARCHAR(255),
    pin VARCHAR(255)
);