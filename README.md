# http-simple

This repository is for creating simple REST APIs to test FujiNet network library.

## Building

To compile and create a releasable jar file, run

```bash
./gradlew shadowJar
```

The resultant jar file is in `build/libs/http-simple-0.1-all.jar`

## Running

```bash
# Run on default port 8080
java -jar build/libs/http-simple-0.1-all.jar

# Run with specific port on localhost
MICRONAUT_SERVER_PORT=8081 java -jar build/libs/http-simple-0.1-all.jar

# Run on specific binding and port, using 0.0.0.0 means other service on same network can interact
# as long as your local firewall doesn't block it.
MICRONAUT_SERVER_HOST=0.0.0.0 MICRONAUT_SERVER_PORT=8081 java -jar build/libs/http-simple-0.1-all.jar
```

## Endpoints

### /alphabet/{num}

Alphabet endpoint.

This endpoint will return num letters from the alphabet, cycling every 26 chars.
There is no delay, the result is a plain text resource

### /cab/{num}

Chunked Alphabet endpoint.

This endpoint will also return num letters from the alphabet, using chunked resources, returning each block up to 10
chars at a time, with a 1 second delay between each chunk.
The result is a plain text resource.
