how to run?
1. mvn clean install
2. docker-compose build
3. docker-compose up
4. hit api 
curl --location 'localhost:8080/push' \
--header 'Content-Type: application/json' \
--data 'first message'
