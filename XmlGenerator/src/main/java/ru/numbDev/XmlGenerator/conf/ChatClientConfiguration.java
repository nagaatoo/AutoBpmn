package ru.numbDev.XmlGenerator.conf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.numbDev.XmlGenerator.ai.GigaChatModel;

@Configuration
public class ChatClientConfiguration {

    @Bean
    public ChatClient defaultChatClient(GigaChatModel model) {
        return ChatClient.builder(model).build();
      }

}
