rem remove old data

docker stop user-manager-app-db-container user-manager-app-container
docker rm   user-manager-app-db-container user-manager-app-container

docker network rm user-manager-app-network

rem create new containers and network

docker network create user-manager-app-network

docker run -d -p 53306:3306 --network user-manager-app-network --name user-manager-app-db-container -e MYSQL_ROOT_PASSWORD=root                     user-manager-app-db
docker run -d -p 80:8080    --network user-manager-app-network --name user-manager-app-container    -e DB_HOST_NAME=user-manager-app-db-container   user-manager-app