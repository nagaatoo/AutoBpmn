package com.example.camundaservice.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("loggerDelegate")
public class LoggerDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Выполняется сервисная задача в процессе: {}", execution.getProcessInstanceId());
        LOGGER.info("ID задачи: {}", execution.getCurrentActivityId());
        LOGGER.info("Название задачи: {}", execution.getCurrentActivityName());
        LOGGER.info("Бизнес-ключ процесса: {}", execution.getProcessBusinessKey());
        
        // Логирование всех переменных процесса
        execution.getVariables().forEach((key, value) -> 
            LOGGER.info("Переменная процесса: {} = {}", key, value));
    }
} 