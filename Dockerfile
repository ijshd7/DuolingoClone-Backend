FROM eclipse-temurin:21-jre
WORKDIR /app
COPY duoclone-backend-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=prod"]