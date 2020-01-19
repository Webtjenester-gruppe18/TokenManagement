FROM java:8
ADD /target/tokenmanagement-1.0-SNAPSHOT.jar tokenmanagement.jar
ENTRYPOINT ["java","-jar","tokenmanagement.jar"]