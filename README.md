# README

This is a simple CRUD web application build using NinjaFramework [link|http://www.ninjaframework.org/].
Sample data structure is build for projects, employees, details of which is described below.

## Database

As a backend storage used MySQL schema **projectsapp** with following tables

| Table | Columns | Description |
| ----- | ------- | ----------- |
| projects | id (int11) | Identifier for project |
|          | name (varchar) | Name of project |
|          | customer (varchar) | Customer of the project |
|          | executor (varchar) | Executor of the project |
|          | begin_date (timestamp) | Date when project started |
|          | end_date (timestamp) | Date when project expected to end or ended |
| employee | id (int11) | Identifier of employee |
|          | first_name (varchar) | first name of employee |
|          | last_name (varchar) | last name of employee |
| project_employee | project_id | project at which employees work |
|                  | employee_id | employee who works on project |


## Deployment and Run

! Before you need to create a mysql schema with name **projectsapp**.

1. Development mode - After cloning create a mysql schema **projectsapp**, set _user_ and _password_
in `src/main/java/conf/application.conf` file. You may not need to create the structure of the schema

``` 
   $ mvn ninja:run 
```

will run the application and it will be available at http://localhost:8080/

2. Production mode - As standalone application. You need to create the mysql schema **projectsapp**,
set _user_ and _password_ in `src/main/java/conf/application.conf`.

```
  db.connection.username=user
  db.connection.password=dbuserpwd
```

And then create a `jar` package
```
  $ mvn clean compile package
```

Now you can run this app as standalone with following command
```
java -Dninja.port=9000 -jar projectsapp-1.1.0-SNAPSHOT.jar
```
Project will be available at http://localhost:9000/