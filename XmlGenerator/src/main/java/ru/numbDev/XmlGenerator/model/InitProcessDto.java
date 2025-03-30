package ru.numbDev.XmlGenerator.model;

import java.util.List;

public record InitProcessDto(
    String processName,
    byte[] processFile,
    List<Delegate> delegates
) {

    public record Delegate(
        String delegateId,
        String javaCode
    ) {
    }
}
