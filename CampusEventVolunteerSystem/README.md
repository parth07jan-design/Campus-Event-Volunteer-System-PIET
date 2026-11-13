# Campus Event Volunteer Management System

Java Swing + JDBC + MySQL desktop minor project.

## Included
- Existing Swing application source: `CampusEventVolunteerApp.java`
- JDBC connection helper: `DBConnection.java`
- MySQL schema and demo records: `database.sql`
- Maven project with MySQL Connector/J dependency: `pom.xml`

## Requirements
- JDK 17 or later
- IntelliJ IDEA
- MySQL Server and MySQL Workbench
- Internet access on first Maven build to download MySQL Connector/J

## Step 1 — Create the database
1. Start MySQL Server.
2. Open MySQL Workbench and connect to your local MySQL instance.
3. Open `database.sql`.
4. Execute the complete script (lightning icon).
5. Confirm that schema `campus_event_volunteer` appears in the SCHEMAS panel.

The script creates the tables expected by the current Java source:
`users`, `events`, `volunteers`, and `assignments`.

## Step 2 — Set your MySQL password
`DBConnection.java` defaults to MySQL user `root` with an empty password.
If your MySQL root account has a password, set environment variable
`CAMPUS_DB_PASSWORD` to that password before launching IntelliJ, or edit
the `PASSWORD` default in `DBConnection.java`.

Optional environment variables:
- `CAMPUS_DB_URL`
- `CAMPUS_DB_USER`
- `CAMPUS_DB_PASSWORD`

Restart IntelliJ after changing Windows environment variables.

## Step 3 — Open in IntelliJ IDEA
1. Extract this ZIP to a normal folder.
2. IntelliJ IDEA → File → Open → select the extracted `CampusEventVolunteerSystem` folder.
3. If prompted, choose **Open as Project** and allow Maven to load.
4. Set Project SDK to JDK 17 or later.
5. Wait for Maven dependency download to finish.
6. Open `src/main/java/CampusEventVolunteerApp.java`.
7. Click the green Run icon beside `main()`.

## Demo login
- Username: `admin`
- Password: `admin123`

The login code in this starter application currently compares the password
as plain text. This is suitable only for a local academic demonstration;
do not reuse it for a real production system.

## Current source-code scope / notes
The supplied Java source includes login, dashboard counts, event add/delete/view,
volunteer add/delete/view, and assignment add/delete/view. The event screen has
an UPDATE button but the supplied handler does not implement its update action.
Volunteer and assignment screens do not currently have update actions.
The student-information module is not present in the uploaded Java source.
This package preserves the supplied application and supplies the missing JDBC
connection and database setup; it does not claim those absent handlers/modules
are implemented.
