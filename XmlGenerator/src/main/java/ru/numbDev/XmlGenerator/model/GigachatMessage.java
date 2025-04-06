package ru.numbDev.XmlGenerator.model;

import ru.numbDev.XmlGenerator.model.GigachatInitFunctionResponse.Choice.Message.FunctionCall;

public record GigachatMessage(
                String role,
                String content,
                String name,
                String functionsStateId,
                FunctionCall functionCall) {
        public GigachatMessage(String role, String content) {
                this(role, content, null, null, null);
        }

        public GigachatMessage(String role, String content, String name, String functionsStateId, FunctionCall functionCall) {
                this.role = role;
                this.content = content;
                this.name = name;
                this.functionsStateId = functionsStateId;
                this.functionCall = functionCall;
        }
}
