-- Campus Event Volunteer Management System
-- Run this script in MySQL Workbench.
CREATE DATABASE IF NOT EXISTS campus_event_volunteer
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE campus_event_volunteer;

CREATE TABLE IF NOT EXISTS users (
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS events (
    event_id INT NOT NULL,
    event_name VARCHAR(150) NOT NULL,
    event_date DATE NOT NULL,
    venue VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    PRIMARY KEY (event_id)
);

CREATE TABLE IF NOT EXISTS volunteers (
    volunteer_id INT NOT NULL,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    department VARCHAR(100),
    PRIMARY KEY (volunteer_id),
    UNIQUE KEY uq_volunteer_email (email)
);

CREATE TABLE IF NOT EXISTS assignments (
    assignment_id INT NOT NULL,
    event_id INT NOT NULL,
    volunteer_id INT NOT NULL,
    `role` VARCHAR(120) NOT NULL,
    PRIMARY KEY (assignment_id),
    CONSTRAINT fk_assignment_event
        FOREIGN KEY (event_id) REFERENCES events(event_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_assignment_volunteer
        FOREIGN KEY (volunteer_id) REFERENCES volunteers(volunteer_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- Demo login for initial testing.
-- Change this password after the first successful login.
INSERT INTO users (username, password)
VALUES ('admin', 'admin123')
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- Optional sample event and volunteer records for testing.
INSERT IGNORE INTO events (event_id, event_name, event_date, venue, description)
VALUES (1, 'PIET Tech Fest', '2026-12-15', 'PIET Campus', 'Sample event record');

INSERT IGNORE INTO volunteers (volunteer_id, name, email, phone, department)
VALUES (1, 'Demo Volunteer', 'volunteer@example.com', '9999999999', 'CSE');
