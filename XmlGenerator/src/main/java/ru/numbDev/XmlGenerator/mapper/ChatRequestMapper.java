package ru.numbDev.XmlGenerator.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import ru.numbDev.XmlGenerator.model.GigachatMessageRequest;

@Mapper(componentModel = "spring")
public interface ChatRequestMapper {

    
    @Mapping(target = "role", expression = "java(typeToRole(message.getMessageType()))")
    @Mapping(target = "content", expression = "java(message.getText())")
    GigachatMessageRequest mapMessage(Message message);

    List<GigachatMessageRequest> mapMessages(List<Message> messages);

    default String typeToRole(MessageType messageType) {
        return messageType == MessageType.TOOL ? "function" : messageType.getValue();
    }
}
