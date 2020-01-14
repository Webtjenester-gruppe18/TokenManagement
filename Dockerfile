FROM java:8
EXPOSE 8080
ADD /target/tokenmanagement-1.0-SNAPSHOT.jar tokenmanagement.jar
ENTRYPOINT ["java","-jar","tokenmanagement.jar"]