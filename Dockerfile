# Используем базовый образ с OpenJDK
FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar-файл с приложением
COPY target/BusBookingSystemApplication-0.0.1-SNAPSHOT.jar app.jar

# Определяем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]