package ru.numbDev.XmlGenerator.model;

import java.util.List;
import java.util.Map;

public record FunctionContent(
    String name,
    String description,
    Parameters parameters,
    Parameters returnParameters,
    List<FewShotExample> fewShotExamples
) {
    public record Parameters(
        String type,
        Map<String, PropertyFields> properties,
        List<String> required
    ) {}

    public record PropertyFields(
        String type,
        String description
    ) {}

    public record FewShotExample(
        String request,
        Map<String, PropertyFields> params
    ) {}
}
