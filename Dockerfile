FROM maven:3.6.0-jdk-11-slim
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
WORKDIR /home/app/target
ENTRYPOINT ["java","-jar","ToDoList-1.0.0.jar"]