# ABC Company Customer API
With the ABC Company Customer API a customer can register and open a new account remotely

## Installation
Clone the repository and run mvn clean install from the root directory.
You can make use of Maven Wrapper (in the root directory) if you don't have Maven installed.
#### On Unix
./mvnw clean install (on Unix)
#### On Windows
mvnw.cmd clean install

## Usage
### Run the application
Go to the target directory and run the application.
#### On Unix
java -jar abc-company-0.0.1-SNAPSHOT.jar
#### On Windows
java abc-company-0.0.1-SNAPSHOT.jar

### Use the ABC Company Customer API
#### Register endpoint
curl -X POST -H "Content-Type: application/json" -d '{"name": "John Doe","address": "Anonymous Street 100, New York","dateofbirth": "1999-01-31","iddocumentnumber": "Passport12345","username": "kleineman85"}' http://localhost:8080/api/v1/register
#### Logon endpoint
curl -X POST -H "Content-Type: application/json" -d '{"username": "kleineman85","password": "changeme"}' http://localhost:8080/api/v1/logon

