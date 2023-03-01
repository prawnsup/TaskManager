# TaskManager
A task manager REST API made with Spring boot using JPA and Spring Securtiy with JWT token authentication. Can be used as a template for rest apis with relational databases and used to create full stack applications.

# How to set up

First ensure that mysql is downloaded https://www.mysql.com/downloads/

Then run and initialise mysql.

Change application properties ,which is located in resources folder, based on username and password.

You might also need to update the hostname in the url for the database since this was developed on a Mac M1 machine where the defaults are different(using UNIX sockets):

from:

jdbc:mysql://mbs.local:3306/task_manager

to:

jdbc:mysql://localhost:3306/task_manager

Then clone this repository.

Run the following commands within root directory:

./mvnw verify #Ensures that everything works

On a side note , a lot of database connection errors can be fixed by simply checking the mysql server status.Then either starting or restarting the server like so:

mysql.server status

mysql.server start

mysql.server restart

If everything works as expected then run:
./mvnw spring-boot:run -X #(-X helps with error tracing and debug is enabled by default in application properties)

# Features and API documentation 

## To register and login with a user send a request containing a json object like shown in postman here:
<img width="677" alt="Screenshot 2023-02-27 at 23 05 24" src="https://user-images.githubusercontent.com/75863764/221709445-70dd5811-9a65-4da5-8e08-0ca522c12fe3.png">

## After a succesful login request a bearer authentication token is returned in the body:

<img width="1013" alt="Screenshot 2023-02-27 at 23 06 41" src="https://user-images.githubusercontent.com/75863764/221709598-32f315cd-d1b7-4cb8-9544-a168fee1c5e2.png">

## Now we can use this token for the rest of the requests like so:
<img width="1011" alt="Screenshot 2023-02-27 at 23 08 04" src="https://user-images.githubusercontent.com/75863764/221709780-f736742f-8332-406e-bc85-e10403a13c32.png">

##  Here an empty json objects returned since user doesnt have any tasks. But we can add tasks like shown below:
<img width="889" alt="Screenshot 2023-02-27 at 23 19 58" src="https://user-images.githubusercontent.com/75863764/221711490-4a7a3c4a-fa55-4222-9a10-6b6bbea04615.png">

##  Here is added task!
<img width="1001" alt="Screenshot 2023-02-27 at 23 39 30" src="https://user-images.githubusercontent.com/75863764/221714126-8bd4729c-ecc0-4cee-a64b-8c01044b5222.png">

There is more that can be done including updating tasks, deleting them and so on.I have documented the REST API endpoints fairly well so I hope what I wanted was made very clear.

# Future improvements and a bit of self reflection

I undertook this project to understand Java web development with Spring Boot and also REST APIs. I plan to use the rest API here to implement a full stack web app with React or Angular in the future.

Instead I ended up understanding how much I dont know.

I hope to implement refresh and access tokens which will be stored using Redis , I have been experimenting with creating my own filters extended from the do once filter class. Currently there is no remember me functionality but that is definitely  going to be implemented very soon!

Just after I figure out unit tests for bearer tokens because I have been working on that for too many hours...
