package ru.numbDev.XmlGenerator.ai;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ru.numbDev.XmlGenerator.model.FunctionContent;
import ru.numbDev.XmlGenerator.model.GigachatMessage;
import ru.numbDev.XmlGenerator.model.ParamModel;
import ru.numbDev.XmlGenerator.model.FunctionContent.FewShotExample;
import ru.numbDev.XmlGenerator.model.FunctionContent.Parameters;

public abstract class AbstractGigachatMessageDecorator implements Message {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public Map<String, Object> getMetadata() {
        return Map.of();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.TOOL; // пока нет другого типа
    }

    public abstract GigachatMessage getGigachatMessage();

    public FunctionContent getFunctionCall() {
        return new FunctionContent(
            getFunctionName(),
            getBodyRequest(),
            getParametersDescription(),
            getReturnParameters(),
            getFewShotExamples()
        );
    }

    protected abstract String getFunctionName();
    protected abstract String getBodyRequest();
    protected abstract ParamModel getParamModel();
    protected abstract Parameters getParametersDescription();
    protected abstract Parameters getReturnParameters();
    protected abstract List<FewShotExample> getFewShotExamples();
    
}