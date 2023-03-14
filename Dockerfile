FROM amazoncorretto:17
ADD ./target/U-Impact-0.0.1-SNAPSHOT.jar U-Impact-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "U-Impact-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080