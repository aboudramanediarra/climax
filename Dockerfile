FROM openjdk:11
ADD target/climax-0.0.1-SNAPSHOT.jar climax-demo.jar
ENTRYPOINT ["java","-jar","climax-demo.jar"]