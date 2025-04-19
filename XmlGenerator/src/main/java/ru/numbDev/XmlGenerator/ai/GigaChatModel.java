package ru.numbDev.XmlGenerator.ai;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chat.giga.client.GigaChatClient;
import chat.giga.model.completion.ChatMessage;
import chat.giga.model.completion.ChatMessage.Role;
import chat.giga.model.completion.CompletionRequest;
import chat.giga.model.completion.CompletionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.numbDev.XmlGenerator.mapper.ChatRequestMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class GigaChatModel implements ChatModel {

    private final GigaChatClient client;
    private final ChatRequestMapper chatRequestMapper;
    private final ObjectMapper objectMapper;

    private String getPrompt(byte[] prompt) {
        return new String(prompt, StandardCharsets.UTF_8);
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        var message = (AbstractGigachatMessageDecorator) prompt.getInstructions().get(0);

        callNewFuction(message);
        // var generatedParams = buildParamsForFunction(message);
        // generatedParams = addGeneratedParamsToModel(generatedParams, message);
        // callFuction(message, generatedParams);
        // String answer =
        // generatedParams.getBody().choices().get(0).message().content();
        return ChatResponse
                .builder()
                .generations(List.of(new Generation(new AssistantMessage("foo")))) // answer
                .build();
    }

    private CompletionResponse buildParamsForFunction(AbstractGigachatMessageDecorator message) {
        return client.completions(
                CompletionRequest.builder()
                        .model(getMyModel())
                        .messages(List.of(message.getGigachatExampleMessage()))
                        .functions(List.of(message.getFunctionCall()))
                        .build());
    }

    private CompletionResponse addGeneratedParamsToModel(
            CompletionResponse generatedParams,
            AbstractGigachatMessageDecorator message) {
        String arguments = null;
        try {
            arguments = objectMapper
                    .writeValueAsString(generatedParams.choices().get(0).message().functionCall().arguments());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return client.completions(
                CompletionRequest.builder()
                        .model(getMyModel())
                        .messages(
                                List.of(
                                        message.getGigachatExampleMessage(),
                                        ChatMessage.builder()
                                                .role(Role.ASSISTANT)
                                                .content(arguments)
                                                .functionsStateId(generatedParams.choices().get(0).message().functionsStateId())
                                                .build(),
                                        ChatMessage.builder()
                                                .role(Role.FUNCTION)
                                                .content(arguments)
                                                .name(message.getFunctionName())
                                                .build()
                                        ))
                        .functions(List.of(message.getFunctionCall()))
                        .build());
    }

    private void callFuction(AbstractGigachatMessageDecorator message, CompletionResponse generatedParams) {
        var response = client.completions(
                CompletionRequest.builder()
                        .model(getMyModel())

                        // .messages(List.of(message.getGigachatMessage(generatedParams.choices().get(0).message().functionsStateId())))
                        // .functionCall(message.getFunctionCall())
                        .build());
        System.out.println(generatedParams.choices().get(0).message().content());
    }

    private void callNewFuction(AbstractGigachatMessageDecorator message) {
        var proto = client.completions(
                CompletionRequest.builder()
                        .model(getMyModel())
                        .messages(message.getRequestMessages())
                        .functionCall("auto")
                        .functions(List.of(message.getFunctionCall()))
                        .build());
        
        
        System.out.println(proto.choices().get(0).message().content());
        System.out.println(proto.choices().get(0).message().functionCall().arguments());
    }

    private String getMyModel() {
        return "GigaChat-2-Max";
    }
}
