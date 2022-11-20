# user-manager-app
Simple application to maintain a user list

Tecnhologies used:

#### Spring boot
#### Spring MVC
#### thymeleaf
#### Hibernate
#### MySQL
#### Docker

To build it, execute `build-docker-images.cmd` then `run-docker-compose.cmd` (on Windows).
On Linux, execute these commands instead (in the root project directory):
```
mvn clean install

docker image build -t user-manager-app .
docker image build -t user-manager-app-db mysql

docker-compose -p user-manager-suite up
```
