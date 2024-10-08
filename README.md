# Personal

This Repo contains my personal pet projects. These projects are related to my professional works and learnings.

Below are the projects in this repo.

1. **Profile MS**: This is a reactive spring boot java microservice. 
2. **Tracing**: This is a spark scala opentelemetry project about generating W3C traces for each row of the dataframe. 

## Profile MS
In my first company I had received training as full stack developer. Among other things I had learned basics about Spring Boot Java MS.
I was more interested in Spring Boot Java and during training itself I came to know about areas of improvement.
As a next step I started learning about reactive spring boot java because of it's better performance.
This was a small pet project I had created at that time to showcase my learnings.  

### Setup
Postgres needs to be setup and accordingly the details of credentials and postgres url need to be updated in [application.yml](./profile/src/main/resources/application.yml)  
After that `profile` table needs to be created in database and some rows need to be saved in it by running the script below.

```postgresql
drop table if exists profile;
create table profile (
    id serial primary key,
    email varchar(32) not null unique,
    password varchar(32) not null,
    name varchar(32),
    dob date,
    number varchar(16)
);
insert into profile
(email, password, name, dob, number)
values (
    'rohit@gmail.com',
    'password',
    'Rohit',
    '1999-03-13',
    '1234567890'
);
insert into profile
(email, password, name, dob, number)
values (
    'lalit@gmail.com',
    'password',
    'Lalit',
    '1999-03-13',
    '1234567890'
);
```

Now the application can be started.

### Rest Calls to Profile MS

1. **Get All**
   This returns all the profiles in the table. 
   1. The data is returned as a stream of profiles with `200 Ok`.
   2. If no profiles are found then `404 Not Found` is returned. 
   ```curl
   curl --location 'http://localhost:8001/profile/get/all'
   ```

2. **Get**
   This returns a particular profile in the table.
   1. If found the data is returned with `302 Found`.
   2. If not found then `404 Not Found` is returned.
   ```curl
   curl --location 'http://localhost:8001/profile/get/lalit@gmail.com'
   ```

3. **Put**
   This updates a particular profile in the table.
   1. Firstly it validates if the new details sent in the request body are valid. If validation fails `400 Bad Request` is returned with reason.
      1. email : this field needs to be valid and not blank
      2. name : at least 1 character in name ; at most 31 characters in name ; only blank spaces and alphabets allowed
      3. dob : date of birth should be in the past as per format yyyy-MM-dd ; age as per dob cannot be less than 16 years
      4. number : should be only 10 digits
   2. If email does not exist `404 Not Found` is returned.
   3. Else data is updated and `200 Ok` returned.
   ```curl
   curl --location --request PUT 'http://localhost:8001/profile/put' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "email": "shahwezjob+1@gmail.com",
   "name": "alpha"
   }'
   ```

## Tracing
This project contains two modules.
1. **TraceDF Library**: This library generates W3C traces for each row of the dataframe.
2. **TraceDF Example**: This project shows the usage of TraceDF library.

For more details go to [README.md of Tracing Project](./tracing/README.md).