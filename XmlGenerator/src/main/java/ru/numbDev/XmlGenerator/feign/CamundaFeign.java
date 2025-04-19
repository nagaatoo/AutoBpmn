package ru.numbDev.XmlGenerator.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// import ru.numbDev.XmlGenerator.model.InitProcessDto;

@FeignClient(name = "camunda-client", url = "http://localhost:8081/")
public interface CamundaFeign {

    @PostMapping("/api/v1/process-definitions/{processDefinitionId}/start")
    void startProcess(@PathVariable String processDefinitionId, @RequestBody Map<String, Object> variables);

    // @PostMapping("/api/process/init")
    // void initProcess(@RequestBody InitProcessDto initProcessDto);
}
