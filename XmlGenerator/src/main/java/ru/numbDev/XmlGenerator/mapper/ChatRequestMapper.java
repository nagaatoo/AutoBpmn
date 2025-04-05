package ru.numbDev.XmlGenerator.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import ru.numbDev.XmlGenerator.model.GigachatMessage;

@Mapper(componentModel = "spring")
public interface ChatRequestMapper {

    
    // @Mapping(target = "role", expression = "java(typeToRole(message.getMessageType()))")
    // @Mapping(target = "content", expression = "java(message.getText())")
    // GigachatMessage mapMessage(Message message);

    // List<GigachatMessage> mapMessages(List<Message> messages);

    // default String typeToRole(MessageType messageType) {
    //     return messageType == MessageType.TOOL ? "function" : messageType.getValue();
    // }
}
