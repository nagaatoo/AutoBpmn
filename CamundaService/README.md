# Camunda 7 Spring Boot Сервис

Это демонстрационное приложение, которое показывает интеграцию Camunda 7 BPM Engine с Spring Boot.

## Требования

- Java 11+
- Maven 3.6+

## Запуск приложения

```bash
mvn clean spring-boot:run
```

После запуска приложение будет доступно по адресу: http://localhost:8080/camunda-service

## Доступные эндпоинты

- **Camunda Web-интерфейс**: http://localhost:8080/camunda-service
  - Логин: demo
  - Пароль: demo

- **Консоль H2 БД**: http://localhost:8080/camunda-service/h2-console
  - JDBC URL: jdbc:h2:mem:camunda
  - Пользователь: sa
  - Пароль: password

- **REST API**:
  - POST http://localhost:8080/camunda-service/api/process/start - Запуск нового процесса
  - GET http://localhost:8080/camunda-service/api/process/tasks - Получение списка задач
  - POST http://localhost:8080/camunda-service/api/process/tasks/{taskId}/complete - Завершение задачи

## Структура проекта

- `src/main/java/com/example/camundaservice/CamundaServiceApplication.java` - главный класс приложения
- `src/main/java/com/example/camundaservice/delegate/LoggerDelegate.java` - делегат для сервисной задачи
- `src/main/java/com/example/camundaservice/controller/ProcessController.java` - REST-контроллер для взаимодействия с процессами
- `src/main/resources/application.properties` - конфигурация приложения
- `src/main/resources/bpmn/sample-process.bpmn` - пример BPMN-процесса

## Пример использования

1. Запустите новый процесс:
```bash
curl -X POST "http://localhost:8080/camunda-service/api/process/start?businessKey=test123"
```

2. Получите список задач:
```bash
curl "http://localhost:8080/camunda-service/api/process/tasks?assignee=demo"
```

3. Завершите задачу (замените {taskId} на ID задачи из предыдущего шага):
```bash
curl -X POST "http://localhost:8080/camunda-service/api/process/tasks/{taskId}/complete"
``` 