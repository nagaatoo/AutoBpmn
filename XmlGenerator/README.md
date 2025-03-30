# XmlGenerator с поддержкой динамических делегатов Camunda

## Динамическая загрузка Java делегатов в Camunda без перезагрузки приложения

### Как это работает

1. **Компиляция кода во время выполнения**: Система использует Groovy для компиляции Java-кода "на лету"
2. **Регистрация в Camunda BPM**: Скомпилированные делегаты регистрируются в ProcessEngine
3. **Обновление кэша процессов**: Происходит сброс кэша определений процессов

### API для работы с делегатами

#### 1. Регистрация делегата

```
POST /api/delegates
{
  "id": "myDelegateId",
  "code": "package com.example; import org.camunda.bpm.engine.delegate.DelegateExecution; import org.camunda.bpm.engine.delegate.JavaDelegate; public class MyDelegate implements JavaDelegate { @Override public void execute(DelegateExecution execution) { // Логика делегата } }"
}
```

#### 2. Удаление делегата

```
DELETE /api/delegates/{delegateId}
```

#### 3. Генерация и регистрация делегатов по BPMN

```
POST /api/delegates/generate
{
  "processId": "myProcess",
  "processXml": "<bpmn:definitions xmlns:bpmn=...>...</bpmn:definitions>"
}
```

### Использование в BPMN

В BPMN-процессе используйте expression для вызова делегата:

```xml
<serviceTask id="serviceTask" name="Мой сервис"
    camunda:delegateExpression="${myDelegateId}">
</serviceTask>
```

### Примечания и ограничения

1. **Классы из внешних библиотек**: Делегат может использовать только классы, доступные в classpath приложения.
2. **Зависимые бины**: Внедрение зависимостей через @Autowired не поддерживается в динамических делегатах.
3. **Тестирование**: Тщательно тестируйте делегаты перед использованием в production. 