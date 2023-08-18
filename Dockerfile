FROM openjdk:11
WORKDIR /app
COPY target/UploadFile-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 80
CMD ["java","-jar","app.jar"]