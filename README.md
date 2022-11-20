# user-manager-app
Simple application to maintain a user list

Tecnhologies used:

- Spring boot
- Spring MVC
- Thymeleaf
- Hibernate
- MySQL
- Docker

To build it, checkout the project then execute [build-docker-images.cmd](https://github.com/danilagalimov/user-manager-app/blob/master/build-docker-images.cmd) then [run-docker-compose.cmd](https://github.com/danilagalimov/user-manager-app/blob/master/run-docker-compose.cmd) (on Windows).
On Linux, execute these commands instead (in the root project directory):
```
mvn clean install

docker image build -t user-manager-app .
docker image build -t user-manager-app-db mysql

docker-compose -p user-manager-suite up
```
