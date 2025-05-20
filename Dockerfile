FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src/ src/

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre

RUN addgroup --system javauser && adduser --system --ingroup javauser javauser

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN chown -R javauser:javauser /app

USER javauser

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
