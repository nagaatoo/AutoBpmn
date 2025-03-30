package com.example.camundaservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.camundaservice.facade.ProcessFacade;
import com.example.camundaservice.model.InitProcessDto;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessFacade processFacade;

    @PostMapping("/init")
    public ResponseEntity<String> postMethodName(@RequestBody InitProcessDto process) {
        return ResponseEntity.ok(processFacade.initProcess(process));
    }
    

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startProcess(@RequestParam(required = false) String businessKey) {
        Map<String, Object> variables = new HashMap<>();
        
        ProcessInstance processInstance = processFacade.startProcess("sample-process", businessKey, variables);
        
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstance.getProcessInstanceId());
        response.put("businessKey", processInstance.getBusinessKey());
        response.put("status", "started");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/tasks")
    public ResponseEntity<List<Map<String, Object>>> getTasks(@RequestParam(required = false) String assignee) {
        List<Task> tasks = processFacade.getTasks(assignee);
        List<Map<String, Object>> result = processFacade.mapTasksToDto(tasks);
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(@PathVariable String taskId) {
        Task task = processFacade.getTask(taskId);
        
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        
        processFacade.completeTask(taskId, null);
        
        Map<String, Object> response = new HashMap<>();
        response.put("taskId", taskId);
        response.put("status", "completed");
        
        return ResponseEntity.ok(response);
    }
} 