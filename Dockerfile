//here is the new update today
FROM azul/zulu-openjdk:17
WORKDIR /app
COPY target/UploadFile-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 80
CMD ["java","-jar","app.jar"]
