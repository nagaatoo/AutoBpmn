package ru.numbDev.XmlGenerator.conf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import chat.giga.client.GigaChatClient;
import chat.giga.client.auth.AuthClient;
import chat.giga.client.auth.AuthClientBuilder.OAuthBuilder;
import chat.giga.model.Scope;
import ru.numbDev.XmlGenerator.ai.GigaChatModel;

@Configuration
public class ChatClientConfiguration {

    @Value("${sber.auth.token}")
    private String authToken;

    @Value("${sber.auth.clientId}")
    private String clientId;

    @Bean
    public ChatClient defaultChatClient(GigaChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean
    public GigaChatClient gigaChatClient() {
        return GigaChatClient.builder()
                .authClient(AuthClient.builder()
                        .withOAuth(OAuthBuilder.builder()
                                .scope(Scope.GIGACHAT_API_PERS)
                                .authKey(authToken)
                                .clientId(clientId)
                                .build())
                        .build())
                .readTimeout(500)
                .connectTimeout(500)
                .build();
    }
}
