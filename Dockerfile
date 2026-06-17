# Step 1: Maven aur Java 11 ka use karke project ko build karenge
FROM maven:3.8.5-openjdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Sirf taiyar jar file ko chalane ke liye choti image use karenge
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]