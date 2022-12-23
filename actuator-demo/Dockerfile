FROM openjdk:19
EXPOSE 8080
ARG JAR_FILE
ADD target/${JAR_FILE} /actuator-demo.jar
ENTRYPOINT ["java", "-jar","/actuator-demo.jar"]