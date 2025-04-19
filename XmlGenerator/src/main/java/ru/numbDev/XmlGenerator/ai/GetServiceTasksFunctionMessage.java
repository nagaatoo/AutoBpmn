package ru.numbDev.XmlGenerator.ai;

import java.util.List;
import java.util.Map;

import chat.giga.model.completion.ChatFunctionFewShotExample;
import chat.giga.model.completion.ChatFunctionParameters;
import chat.giga.model.completion.ChatFunctionParametersProperty;
import chat.giga.model.completion.ChatMessage;
import chat.giga.model.completion.ChatMessage.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetServiceTasksFunctionMessage extends AbstractGigachatMessageDecorator {

    private static final String PROCESS_XML_NAME = "process_xml";

    private final String processXml;

    @Override
    protected String getDescription() {
        return """
                Поиск сервисных задач в тесте bpmn процесса и возвращение их содержимого вместе с тегами <bpmn:serviceTask>.""";
    }

    @Override
    public String getText() {
        return "Найди все теги <bpmn:serviceTask> в bpmn процессе и верни их содержимое вместе с тегами <bpmn:serviceTask> включительно. "
                +
                "В качестве имени делегата используй значение атрибута camunda:delegateExpression без #{}. Текст процесса: "
                + processXml;
    }

    @Override
    protected String getFunctionName() {
        return "get_service_tasks";
    }

    @Override
    protected String getBodyExampleRequest() {
        return "Найди все теги <bpmn:serviceTask> в bpmn процессе и верни их содержимое вместе с тегами <bpmn:serviceTask> включительно. "
                +
                "В качестве имени делегата используй значение атрибута camunda:delegateExpression без #{}. " +
                "Текст процесса: <bpmn:service><bpmn:serviceTask id=\"Activity_06uvll5\" name=\"Сгенерировать акт\" camunda:delegateExpression=\"#{generateDelegate}\">foo</bpmn:serviceTask></bpmn:service>";
    }

    @Override
    protected ChatFunctionParameters getParametersDescription() {
        return ChatFunctionParameters.builder()
                .type("object")
                .properties(
                        Map.of(
                                PROCESS_XML_NAME, ChatFunctionParametersProperty.builder()
                                        .type("string")
                                        .description("Текст bpmn процесса")
                                        .build()

                                // "delegates", ChatFunctionParametersProperty.builder()
                                //         .type("map")
                                //         .description("Карта извлеченных делегатов")
                                //         .items(
                                //                 Map.of(
                                //                         "name", ChatFunctionParametersProperty.builder()
                                //                                 .type("string")
                                //                                 .description("Имя делегата из тега delegateExpression")
                                //                                 .build(),
                                //                         "code", ChatFunctionParametersProperty.builder()
                                //                                 .type("string")
                                //                                 .description(
                                //                                         "Текст, находящийся между <bpmn:serviceTask> и </bpmn:serviceTask> включительно")
                                //                                 .build()))
                                //         .build()
                                        ))
                .required(List.of(PROCESS_XML_NAME))
                .build();
    }

    @Override
    protected ChatFunctionParameters getReturnParameters() {
        return ChatFunctionParameters.builder()
                .type("object")
                .properties(
                        Map.of(
                                "delegates", ChatFunctionParametersProperty.builder()
                                        .type("object")
                                        .description("Карта извлеченных делегатов")
                                        .items(
                                                Map.of(
                                                        "name", ChatFunctionParametersProperty.builder()
                                                                .type("string")
                                                                .description("Имя делегата из тега delegateExpression")
                                                                .build(),
                                                        "code", ChatFunctionParametersProperty.builder()
                                                                .type("string")
                                                                .description(
                                                                        "Текст, находящийся между <bpmn:serviceTask> и </bpmn:serviceTask> включительно")
                                                                .build()))
                                        .build(),
                                "error", ChatFunctionParametersProperty.builder()
                                        .type("string")
                                        .description("Возвращается при возникновении ошибки. Содержит описание ошибки")
                                        .build()))
                .build();
    }

    @Override
    protected List<ChatFunctionFewShotExample> getFewShotExamples() {
        return List.of(
                ChatFunctionFewShotExample.builder()
                        .request(getBodyExampleRequest())
                        .params(
                                Map.of(
                                        PROCESS_XML_NAME,
                                        "<bpmn:service><bpmn:serviceTask id=\"Activity_06uvll5\" name=\"Сгенерировать акт\" camunda:delegateExpression=\"#{generateDelegate}\">foo</bpmn:serviceTask></bpmn:service>"))
                        .build());
    }

    @Override
    protected ChatFunctionParameters getParamModel() {
        return ChatFunctionParameters.builder()
                .build();
        // return new GetServiceTaskFunctionParamModel(processXml);
    }

    @Override
    protected ChatMessage getRoleSystem() {
        return ChatMessage.builder()
                .role(Role.SYSTEM)
                .content("Ты помощник по разработке bpmn процессов. Ты должен вернуть карту извлеченных делегатов.")
                .build();
    }

}
