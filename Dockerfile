FROM openjdk:20
WORKDIR /app
COPY target/UploadFile-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java","-jar","app.jar"]