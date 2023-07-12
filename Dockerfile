FROM openjdk:7-alpine
WORKDIR /app
COPY out/artifacts/UploadFile_jar/UploadFile.jar /app/app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]
