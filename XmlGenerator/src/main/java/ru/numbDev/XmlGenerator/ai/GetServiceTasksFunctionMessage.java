package ru.numbDev.XmlGenerator.ai;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import ru.numbDev.XmlGenerator.model.FunctionContent.FewShotExample;
import ru.numbDev.XmlGenerator.model.FunctionContent.Parameters;
import ru.numbDev.XmlGenerator.model.FunctionContent.PropertyFields;
import ru.numbDev.XmlGenerator.model.GetServiceTaskFunctionParamModel;
import ru.numbDev.XmlGenerator.model.ParamModel;

@Builder
@Getter
public class GetServiceTasksFunctionMessage extends AbstractGigachatMessageDecorator {

    private static final String PROCESS_XML_NAME = "process_xml";

    private final String processXml;

    @Override
    public String getText() {
        return "Найди все теги <bpmn:serviceTask> в bpmn процессе и верни их содержимое вместе с тегами. Текст процесса: " + processXml;
    }

    @Override
    protected String getFunctionName() {
        return "get_service_tasks";
    }

    @Override
    protected String getBodyExampleRequest() {
        return "Найди все теги <bpmn:serviceTask> в bpmn процессе и верни их содержимое вместе с тегами. Текст процесса: <bpmn:service><bpmn:serviceTask>foo</bpmn:serviceTask></bpmn:service>";
    }

    @Override
    protected Parameters getParametersDescription() {
        return new Parameters(
                "object",
                Map.of(
                        PROCESS_XML_NAME,
                        new PropertyFields(
                                "string",
                                "Текст bpmn процесса")),
                List.of(PROCESS_XML_NAME));
    }

    @Override
    protected Parameters getReturnParameters() {
        return new Parameters(
                "object",
                Map.of(
                        "name", new PropertyFields(
                                "string",
                                "Имя делегата из тега delegateExpression"),
                        "code", new PropertyFields(
                                "string",
                                "Код, находящийся между <bpmn:serviceTask> и </bpmn:serviceTask> включительно")),
                List.of("name", "code"));
    }

    @Override
    protected List<FewShotExample> getFewShotExamples() {
        return List.of(
                new FewShotExample(
                        getBodyExampleRequest(),
                        Map.of(PROCESS_XML_NAME,
                                "<bpmn:service><bpmn:serviceTask>foo</bpmn:serviceTask></bpmn:service>")));
    }

    @Override
    protected ParamModel getParamModel() {
        return new GetServiceTaskFunctionParamModel(processXml);
    }

    @Override
    protected String getDescription() {
        return """
                Поиск сервисных задач в тесте bpmn процесса и возвращение их содержимого вместе с тегами <bpmn:serviceTask>.""";
    }

}
