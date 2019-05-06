To Run the application

1)Need to have mongodb running on :

Host: 127.0.0.1 Port: 27017

2)I have IntelliJ IDEA and Gradle installed on the system

In IntelliJ Open the project

gradle clean
For Jar: In intellij gradle option assemble will generate "myRetail-0.0.1-SNAPSHOT.jar" 
in build/libs folder


//GET endpoint Type the below URl in the Web Browser: http://localhost:8080/products/{id}

example: http://localhost:8080/products/13860428

//For test Update: In postman: Send a PUT request to http://localhost:8080/products/{id}

Example:- http://localhost:8080/products/13860428

with json body:
{
   "value": 40,
    "currency_code": "INR"
}



3)Unit TestCases in test folder
Intellij you can run test
