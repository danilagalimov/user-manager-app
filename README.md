# user-manager-app
Simple application to maintain a user list

Tecnhologies used:

- Spring Boot
- Spring MVC
- Thymeleaf
- Hibernate
- MySQL
- Docker

To run it, checkout the project then execute [build-docker-images.cmd](https://github.com/danilagalimov/user-manager-app/blob/master/build-docker-images.cmd) then [run-docker-compose.cmd](https://github.com/danilagalimov/user-manager-app/blob/master/run-docker-compose.cmd) (on Windows).
On Linux, execute these commands instead (in the root project directory):
```
mvn clean install

docker image build -t user-manager-app .
docker image build -t user-manager-app-db mysql

docker-compose -p user-manager-suite up
```
Application should be available on http://localhost 

If you want to run the container without docker-compose, execute ([run-docker-images.cmd](https://github.com/danilagalimov/user-manager-app/blob/master/run-docker-images.cmd)). It stops all the containers then starts them again without using docker-compose
