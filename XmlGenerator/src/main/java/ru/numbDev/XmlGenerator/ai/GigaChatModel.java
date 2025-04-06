package ru.numbDev.XmlGenerator.ai;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.numbDev.XmlGenerator.conf.GigaChatOptions;
import ru.numbDev.XmlGenerator.conf.GigaChatProperties;
import ru.numbDev.XmlGenerator.feign.GigaChatAuthFeign;
import ru.numbDev.XmlGenerator.feign.GigaChatFeign;
import ru.numbDev.XmlGenerator.mapper.ChatRequestMapper;
import ru.numbDev.XmlGenerator.model.AccessToken;
import ru.numbDev.XmlGenerator.model.GigachatInitFunctionResponse;
import ru.numbDev.XmlGenerator.model.GigachatMessage;
import ru.numbDev.XmlGenerator.model.GigachatPromptRequest;
import ru.numbDev.XmlGenerator.model.GigachatPromptUserRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class GigaChatModel implements ChatModel {

    private static final String BEARER = "Bearer";

    @Value("${sber.auth.token}")
    private String authToken;

    @Value("${sber.auth.clientId}")
    private String clientId;

    @Value("${sber.auth.scope}")
    private String scope;

    private final GigaChatFeign feignClient;
    private final GigaChatAuthFeign authFeign;
    private final GigaChatProperties gigaChatProperties;
    private final ChatRequestMapper chatRequestMapper;
    private final ObjectMapper objectMapper;

    private AccessToken accessToken;
    private String currentModel;

    public String requestToken() {
        if (accessToken == null || (System.currentTimeMillis() % 1000) > accessToken.expiresAt()) {
            ResponseEntity<AccessToken> respose = authFeign.getAccessToken(
                    "Basic " + authToken,
                    clientId,
                    Map.of("scope", scope));

            checkStatus(respose);
            accessToken = respose.getBody();
        }

        return BEARER + " " + accessToken.accessToken();
    }

    private String getPrompt(byte[] prompt) {
        return new String(prompt, StandardCharsets.UTF_8);
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        var message = (AbstractGigachatMessageDecorator) prompt.getInstructions().get(0);

        var generatedParams = buildParamsForFunction(message);
        addGeneratedParamsToModel(generatedParams, message);
        callFuction(message);
        // String answer =
        // generatedParams.getBody().choices().get(0).message().content();
        return ChatResponse
                .builder()
                .generations(List.of(new Generation(new AssistantMessage("foo")))) // answer
                .build();
    }

    private GigachatInitFunctionResponse buildParamsForFunction(AbstractGigachatMessageDecorator message) {
        var request = new GigachatPromptRequest(
                getMyModel(),
                List.of(message.getGigachatExampleMessage()),
                "auto", // new GigachatFunctionCallModel(message.getFunctionName(),
                        // message.getParamModel())
                List.of(message.getFunctionCall()),
                1,
                false,
                getDefaultOptions().getMaxTokens(),
                getDefaultOptions().getFrequencyPenalty(),
                0);

        var response = feignClient.initFunction(requestToken(), request);

        checkStatus(response);
        return response.getBody();
    }

    private void addGeneratedParamsToModel(
            GigachatInitFunctionResponse generatedParams,
            AbstractGigachatMessageDecorator message) {
        String arguments = null;
        try {
            arguments = objectMapper
                    .writeValueAsString(generatedParams.choices().get(0).message().functionCall().arguments());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var request = new GigachatPromptRequest(
                getMyModel(),
                List.of(
                        message.getGigachatExampleMessage(),
                        new GigachatMessage(
                                "assistant",
                                arguments,
                                null,
                                generatedParams.choices().get(0).message().functionsStateId(),
                                generatedParams.choices().get(0).message().functionCall()),
                        new GigachatMessage(
                                "function",
                                arguments,
                                message.getFunctionName(),
                                null,
                                null)),
                "auto",
                List.of(message.getFunctionCall()),
                1,
                false,
                getDefaultOptions().getMaxTokens(),
                getDefaultOptions().getFrequencyPenalty(),
                0);

        var response = feignClient.initFunction(requestToken(), request);
        checkStatus(response);
    }

    private void callFuction(AbstractGigachatMessageDecorator message) {
        var response = feignClient.executePrompt(
                requestToken(),
                new GigachatPromptUserRequest(
                        getMyModel(),
                        List.of(message.getGigachatMessage()),
                        "auto",
                        null,
                        1,
                        false,
                        getDefaultOptions().getMaxTokens(),
                        getDefaultOptions().getFrequencyPenalty(),
                        0));
        checkStatus(response);
    }

    @Override
    public ChatOptions getDefaultOptions() {
        return new GigaChatOptions(gigaChatProperties);
    }

    private static void checkStatus(ResponseEntity<?> accessToken) {
        if (accessToken.getBody() == null) {
            throw new IllegalStateException("Tocken is null");
        }

        if (!accessToken.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException(
                    "Received '%d' status code for call"
                            .formatted(accessToken.getStatusCode().value()));
        }
    }

    private String getMyModel() {
        if (StringUtils.isBlank(currentModel)) {
            // var response = feignClient.getModels(requestToken());
            // checkStatus(response);

            // System.out.println("sdfasdf");
            currentModel = "GigaChat-2-Max-preview";
        }

        return currentModel;
    }

}
