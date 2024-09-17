## Build the project
.\mvnw clean package -DskipTests

## Create The Docker Image
docker build -t gateway:v1 .

## Display The created Image
docker images

## Run the Container
docker run -d -p 8036:8036 --name gateway-v1 gateway:v1

## Display The Running Container
docker ps


