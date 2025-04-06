package ru.numbDev.XmlGenerator.model;

import java.util.List;

public record GigachatInitFunctionResponse(
        List<Choice> choices,
        Long created,
        String model,
        String object,
        Usage usage) {
    public record Choice(
            Message message,
            Integer index,
            String finishReason) {
        public record Message(
                String content,
                String role,
                FunctionCall functionCall,
                String functionsStateId) {
            public record FunctionCall(
                    String name,
                    Arguments arguments) {
                public record Arguments(
                        String processXml) { // TODO динамический тип аргументов
                }
            }
        }
    }

    public record Usage(
            Integer promptTokens,
            Integer completionTokens,
            Integer totalTokens,
            Integer precachedPromptTokens) {
    }
}