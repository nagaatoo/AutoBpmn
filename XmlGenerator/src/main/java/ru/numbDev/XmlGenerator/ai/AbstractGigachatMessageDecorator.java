package ru.numbDev.XmlGenerator.ai;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import ru.numbDev.XmlGenerator.model.FunctionContent;
import ru.numbDev.XmlGenerator.model.FunctionContent.FewShotExample;
import ru.numbDev.XmlGenerator.model.FunctionContent.Parameters;
import ru.numbDev.XmlGenerator.model.GigachatMessage;
import ru.numbDev.XmlGenerator.model.ParamModel;

public abstract class AbstractGigachatMessageDecorator implements Message {

    @Override
    public Map<String, Object> getMetadata() {
        return Map.of();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.TOOL; // пока нет другого типа
    }

    public FunctionContent getFunctionCall() {
        return new FunctionContent(
            getFunctionName(),
            getDescription(),
            getParametersDescription(),
            getReturnParameters(),
            getFewShotExamples()
        );
    }

    public GigachatMessage getGigachatMessage() {
        return new GigachatMessage(
                "user",
                getText());
    }

    public GigachatMessage getGigachatExampleMessage() {
        return new GigachatMessage(
                "user",
                getBodyExampleRequest());
    }

    protected abstract String getFunctionName();
    protected abstract String getDescription();
    protected abstract String getBodyExampleRequest();
    protected abstract ParamModel getParamModel();
    protected abstract Parameters getParametersDescription();
    protected abstract Parameters getReturnParameters();
    protected abstract List<FewShotExample> getFewShotExamples();
    
}