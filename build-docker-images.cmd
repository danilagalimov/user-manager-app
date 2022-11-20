rem building java maven
call mvn clean install

rem packing images to docker
docker image build -t user-manager-app .
docker image build -t user-manager-app-db mysql