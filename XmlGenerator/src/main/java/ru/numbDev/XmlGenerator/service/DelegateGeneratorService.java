package ru.numbDev.XmlGenerator.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.numbDev.XmlGenerator.ai.GetServiceTasksFunctionMessage;
import ru.numbDev.XmlGenerator.feign.CamundaFeign;
import ru.numbDev.XmlGenerator.mapper.ChatResultMapper;
import ru.numbDev.XmlGenerator.model.InitProcessDto;
import ru.numbDev.XmlGenerator.model.InitProcessDto.Delegate;
import ru.numbDev.XmlGenerator.model.ServicesResponse;

@Service
@RequiredArgsConstructor
public class DelegateGeneratorService {

    private final ChatClient chatClient;
    private final CamundaFeign camundaFeign;
    private final ResourceLoader resourceLoader;
    private final ChatResultMapper chatResultMapper;

    public String generateForProcess(String fileName, byte[] processXml) {
        String serviceBlocks = getServiceDelegatesFromXml(processXml);
        var serviceBlocksMap = chatResultMapper.mapJsonToServicesResponse(serviceBlocks);
        var delegates = generateDelegatesCode(serviceBlocksMap);
        // camundaFeign.initProcess(buildRequest(fileName, processXml, delegates));

        return serviceBlocks;
    }

    private String getServiceDelegatesFromXml(byte[] processXml) {
        String xmlText = new String(processXml, StandardCharsets.UTF_8);
        return chatClient
                .prompt(new Prompt(
                    GetServiceTasksFunctionMessage.builder()
                        .processXml(xmlText)
                        .build()
                ))
                .call()
                .content();
    }

    private List<Delegate> generateDelegatesCode(List<ServicesResponse> servicesResponses) {
        List<Delegate> delegates = new ArrayList<>();
        String openapiSpec = getOpenapiSpec();

        servicesResponses.forEach(serviceResponse -> {
            String promptText = """
                            Сгенерируй Spring javaDelegat'ы camunda для сервисного таскаописанного ниже.
                            Текст <bpmn:serviceTask>: %s.

                            Алгоритм делегата возьми из тега  <bpmn:documentation>.
                            Ответом верни только java код делегата.
                            Для сетевых вызовов, необходимымх процессу, используй спецификацию openapi: %s
                            """.formatted(serviceResponse.code(), openapiSpec);
            // String promptText = MessageFormat.format(
            //         ,
            //         serviceResponse.code(),
            //         openapiSpec);

            var delegateCode = chatClient
                    .prompt(promptText)
                    .call()
                    .content();
            delegates.add(new Delegate(serviceResponse.name(), delegateCode));
        });

        return delegates;
    }

    private String getOpenapiSpec() {
        try {
            Resource resource = resourceLoader.getResource("classpath:openapi.yaml");
            try (InputStream inputStream = resource.getInputStream()) {
                return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: openapi.yaml", e);
        }
    }

    private InitProcessDto buildRequest(String fileName, byte[] processXml, String delegates) {
        String[] delegatesArray = delegates.split("---");
        return new InitProcessDto(
                fileName,
                processXml,
                Stream.of(delegatesArray)
                        .map(code -> new Delegate(null, code))
                        .toList());
    }

    private String getDelegateId(String code) {
        return null;
    }

    public String testRequestMessage(String message) {
        return chatClient
                .prompt(message)
                .call()
                .content();
    }

}
