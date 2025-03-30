package ru.numbDev.XmlGenerator.ai;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import ru.numbDev.XmlGenerator.model.FunctionContent.FewShotExample;
import ru.numbDev.XmlGenerator.model.FunctionContent.Parameters;
import ru.numbDev.XmlGenerator.model.FunctionContent.PropertyFields;

@Builder
@Getter
public class GetServiceTasksFunctionMessage extends GigachatMessage {

    private final String processXml;

    @Override
    protected String getFunctionName() {
        return "getServiceTasks";
    }

    @Override
    protected String getBodyRequest() {
        return """
        Найди все теги <bpmn:serviceTask> в bpmn процессе и верни их содержимое в формате json.
        """;
    }

    @Override
    protected Parameters getParameters() {
        return new Parameters(
            "object",
            Map.of(
                "processXml", new PropertyFields(
                    "string",
                    "Текст bpmn процесса"
                )
            ),
            List.of("processXml")
        );
    }

    @Override
    protected Parameters getReturnParameters() {
        return new Parameters(
            "object",
            Map.of(
                "name", new PropertyFields(
                    "string",
                    "Имя делегата из тега delegateExpression"
                ),
                "code", new PropertyFields(
                    "string",
                    "Код, находящийся между <bpmn:serviceTask > и </bpmn:serviceTask> включительно"
                )
            ),
            List.of("name", "code")
        );
    }

    @Override
    protected List<FewShotExample> getFewShotExamples() {
        return List.of(
            new FewShotExample(
                "Текст bpmn процесса",
                Map.of("processXml", new PropertyFields("string", "Текст bpmn процесса"))
            )
        );
    }

}
