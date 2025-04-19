package ru.numbDev.XmlGenerator.ai;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import chat.giga.model.completion.ChatFunction;
import chat.giga.model.completion.ChatFunctionFewShotExample;
import chat.giga.model.completion.ChatFunctionParameters;
import chat.giga.model.completion.ChatMessage;
import chat.giga.model.completion.ChatMessage.Role;

public abstract class AbstractGigachatMessageDecorator implements Message {

    @Override
    public Map<String, Object> getMetadata() {
        return Map.of();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.TOOL; // пока нет другого типа
    }

    public ChatFunction getFunctionCall() {
        return ChatFunction.builder()
            .name(getFunctionName())
            .description(getDescription())
            .parameters(getParametersDescription())
            .returnParameters(getReturnParameters())
            .fewShotExamples(getFewShotExamples())
            .build();
    }

    public List<ChatMessage> getRequestMessages() {
        return List.of(
            getRoleSystem(),
            getGigachatMessage()
        );
    }

    public ChatMessage getGigachatMessage() {
        return ChatMessage.builder()
            .role(Role.USER)
            .content(getText())
            .build();
    }

    public ChatMessage getGigachatExampleMessage() {
        return ChatMessage.builder()
            .role(Role.USER)
            .content(getBodyExampleRequest())
            .build();
    }

    protected abstract String getFunctionName();
    protected abstract String getDescription();
    protected abstract String getBodyExampleRequest();
    protected abstract ChatMessage getRoleSystem();
    protected abstract ChatFunctionParameters getParamModel();
    protected abstract ChatFunctionParameters getParametersDescription();
    protected abstract ChatFunctionParameters getReturnParameters();
    protected abstract List<ChatFunctionFewShotExample> getFewShotExamples();
    
}