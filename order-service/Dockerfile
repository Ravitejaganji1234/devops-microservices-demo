FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

COPY pom.xml /app/

RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY . .

RUN mvn clean install -DskipTests

ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/orderdb?createDatabaseIfNotExist=true

EXPOSE 8082

RUN mv target/*.jar target/app.jar

ENTRYPOINT [ "java","-jar","target/app.jar" ]