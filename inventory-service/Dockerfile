FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/inventory-service-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/inventorydb?createDatabaseIfNotExist=true \
    SPRING_DATASOURCE_USERNAME=root \
    SPRING_DATASOURCE_PASSWORD=root

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]