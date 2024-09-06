FROM registry.access.redhat.com/ubi8/openjdk-21:1.20

COPY target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]