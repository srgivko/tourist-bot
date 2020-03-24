# TouristBot
1. Create in PostgreSQL database [**tourist-bot**]

2. Run app

# Test JSON API
http://localhost:8080/swagger-ui.html#/city-controller

_or use curl_

`curl -X GET "http://localhost:8080/cities" -H "accept: */*"`

`curl -X GET "http://localhost:8080/cities/1" -H "accept: */*"`

`curl -X POST "http://localhost:8080/cities" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"info\": \"test\", \"name\": \"test\"}"`

`curl -X PUT "http://localhost:8080/cities" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"id\": 2, \"info\": \"string2323\", \"name\": \"string2323\"}"`

`curl -X DELETE "http://localhost:8080/cities/1" -H "accept: */*"`

# Bot info
username: TouristSimpleBot

token: 1109655834:AAHhZl08BbOtm1jme0xCCOqmpOdQqE_36i8
