# Этап 1: Сборка приложения
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости (кеширование слоев)
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код и собираем пакет
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: Создание финального образа
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Создаем пользователя для безопасности (best practice 2026)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Копируем собранный JAR из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Настройка логирования в консоль для Docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]