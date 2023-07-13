FROM oadoptopenjdk:17-jdk-hotspot
WORKDIR /app
COPY out/artifacts/UploadFile_jar/UploadFile.jar /app/app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]
