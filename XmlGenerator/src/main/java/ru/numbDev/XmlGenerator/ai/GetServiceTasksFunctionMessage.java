package ru.numbDev.XmlGenerator.ai;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import ru.numbDev.XmlGenerator.model.FunctionContent.FewShotExample;
import ru.numbDev.XmlGenerator.model.FunctionContent.Parameters;
import ru.numbDev.XmlGenerator.model.FunctionContent.PropertyFields;
import ru.numbDev.XmlGenerator.model.GetServiceTaskFunctionParamModel;
import ru.numbDev.XmlGenerator.model.GigachatMessage;
import ru.numbDev.XmlGenerator.model.ParamModel;

@Builder
@Getter
public class GetServiceTasksFunctionMessage extends AbstractGigachatMessageDecorator {

    private final String processXml;

    @Override
    public String getText() {
        return "Найди все теги <bpmn:serviceTask> в bpmn процессе и верни их содержимое вместе с тегами. Текст bpmn процесса";
    }

    @Override
    protected String getFunctionName() {
        return "getServiceTasks";
    }

    @Override
    protected String getBodyRequest() {
        return """
                Поиск всех тегов <bpmn:serviceTask> в bpmn процессе и возвращение их содержимого вместе с тегами <bpmn:serviceTask>.
                """;
    }

    @Override
    protected Parameters getParametersDescription() {
        return new Parameters(
                "object",
                Map.of(
                        "processXml", new PropertyFields(
                                "string",
                                "Текст bpmn процесса")),
                List.of("processXml"));
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
                        "Найди все теги <bpmn:serviceTask> в bpmn процессе и верни их содержимое вместе с тегами.",
                        Map.of("processXml",
                                "<bpmn:service><bpmn:serviceTask>foo</bpmn:serviceTask></bpmn:service>")));
    }

    @Override
    public GigachatMessage getGigachatMessage() {
        return new GigachatMessage(
                "user",
                getBodyRequest());
    }

    @Override
    protected ParamModel getParamModel() {
        return new GetServiceTaskFunctionParamModel(processXml);
    }

}
