how to run?
mvn clean install
docker-compose build
docker-compose run

hit api 
curl --location 'localhost:8080/push' \
--header 'Content-Type: application/json' \
--data 'random1'
