package ru.numbDev.XmlGenerator.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import ru.numbDev.XmlGenerator.conf.FeignConfig;
import ru.numbDev.XmlGenerator.model.GigachatInitFunctionResponse;
import ru.numbDev.XmlGenerator.model.GigachatPromptRequest;
import ru.numbDev.XmlGenerator.model.GigachatPromptUserRequest;
import ru.numbDev.XmlGenerator.model.Model;
import ru.numbDev.XmlGenerator.model.ObjectResponse;

@FeignClient(name = "gigachat-client", url = "https://gigachat.devices.sberbank.ru", configuration = FeignConfig.class)
public interface GigaChatFeign {

    /**
     * Get available models
     */
    @GetMapping(path = "/api/v1/models", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ObjectResponse<List<Model>>> getModels(@RequestHeader("Authorization") String auth);

    /**
     * Send prompt to chat
     */
    @PostMapping(path = "/api/v1/chat/completions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GigachatInitFunctionResponse> initFunction( // ModelResponse
            @RequestHeader("Authorization") String auth,
            @RequestBody GigachatPromptRequest promptRequest);

    @PostMapping(path = "/api/v1/chat/completions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> executePrompt(
            @RequestHeader("Authorization") String auth,
            @RequestBody GigachatPromptUserRequest promptRequest);
}
