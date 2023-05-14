#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
RUN apt-get update && apt-get install -y python3
COPY src/main/java/com/chatapi/sigmaapi/assets/thumbnail/extract_images.py extract_images.py
COPY --from=build /target/sigma-api-0.0.1-SNAPSHOT.jar sigma-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","sigma-api.jar", "arg1", "arg2", "&&", "python3", "extract_images.py", "arg1", "arg2"]
