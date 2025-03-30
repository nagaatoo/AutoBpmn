package com.example.camundaservice.facade;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.BadRequestException;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.camundaservice.model.InitProcessDto;
import com.example.camundaservice.service.DynamicDelegateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessFacade.class);

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final TaskService taskService;
    private final DynamicDelegateService dynamicDelegateService;

    /**
     * Запускает новый экземпляр процесса по его ключу
     * 
     * @param processKey  ключ процесса
     * @param businessKey бизнес-ключ (опционально)
     * @param variables   переменные процесса
     * @return созданный экземпляр процесса
     */
    public ProcessInstance startProcess(String processKey, String businessKey, Map<String, Object> variables) {
        LOGGER.info("Запуск процесса: {}, businessKey: {}", processKey, businessKey);

        if (variables == null) {
            variables = new HashMap<>();
        }

        variables.put("startTime", System.currentTimeMillis());

        return runtimeService.startProcessInstanceByKey(
                processKey,
                businessKey,
                variables);
    }

    /**
     * Получает список задач, назначенных на указанного исполнителя
     * 
     * @param assignee исполнитель (может быть null для получения всех задач)
     * @return список задач
     */
    public List<Task> getTasks(String assignee) {
        if (assignee != null && !assignee.isEmpty()) {
            LOGGER.info("Получение задач для пользователя: {}", assignee);
            return taskService.createTaskQuery().taskAssignee(assignee).list();
        } else {
            LOGGER.info("Получение всех задач");
            return taskService.createTaskQuery().list();
        }
    }

    /**
     * Преобразует Task в Map для удобной сериализации в JSON
     * 
     * @param tasks список задач
     * @return список задач в виде Map
     */
    public List<Map<String, Object>> mapTasksToDto(List<Task> tasks) {
        return tasks.stream()
                .map(task -> {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("id", task.getId());
                    taskMap.put("name", task.getName());
                    taskMap.put("assignee", task.getAssignee());
                    taskMap.put("created", task.getCreateTime());
                    taskMap.put("processInstanceId", task.getProcessInstanceId());
                    return taskMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * Получает задачу по идентификатору
     * 
     * @param taskId идентификатор задачи
     * @return задача или null, если не найдена
     */
    public Task getTask(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    /**
     * Завершает задачу
     * 
     * @param taskId    идентификатор задачи
     * @param variables переменные, которые будут установлены при завершении задачи
     */
    public void completeTask(String taskId, Map<String, Object> variables) {
        LOGGER.info("Завершение задачи: {}", taskId);

        if (variables == null) {
            taskService.complete(taskId);
        } else {
            taskService.complete(taskId, variables);
        }
    }

    public String initProcess(InitProcessDto process) {
        String deploymentId = registerProcess(process.processName(), process.processFile());
        process.delegates().forEach(delegate -> {
            dynamicDelegateService.registerDelegate(delegate.delegateId(), delegate.javaCode());
        });

        return deploymentId;
    }

    private String registerProcess(String processName, byte[] processFile) {
        try (InputStream bais = new ByteArrayInputStream(processFile)) {
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                    .name(processName)
                    .addInputStream(processName, bais)
                    .enableDuplicateFiltering(true); // Позволяет избежать повторного развертывания одинаковых процессов

            DeploymentWithDefinitions deployment = deploymentBuilder.deployWithResult();

            Map<String, Object> result = new HashMap<>();
            result.put("deploymentId", deployment.getId());
            result.put("deploymentName", deployment.getName());
            result.put("deploymentTime", deployment.getDeploymentTime());

            List<Map<String, Object>> definitions = new ArrayList<>();
            for (ProcessDefinition definition : deployment.getDeployedProcessDefinitions()) {
                Map<String, Object> defMap = new HashMap<>();
                defMap.put("id", definition.getId());
                defMap.put("key", definition.getKey());
                defMap.put("name", definition.getName());
                defMap.put("version", definition.getVersion());
                defMap.put("resourceName", definition.getResourceName());
                definitions.add(defMap);
            }

            result.put("processDefinitions", definitions);

            LOGGER.info("Процесс успешно развернут: {}, deploymentId: {}",
                    processName, deployment.getId());

            return deployment.getId();
        } catch (IOException ex) {
            throw new BadRequestException("Ошибка при развертывании процесса: " + ex.getMessage());
        }
    }
}