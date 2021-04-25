from adoptopenjdk/openjdk14
COPY ./target/RestApiApp-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]