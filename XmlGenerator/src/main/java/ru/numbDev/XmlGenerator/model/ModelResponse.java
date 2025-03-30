package ru.numbDev.XmlGenerator.model;

import java.util.List;

public record ModelResponse(
    List<Choise> choices,
    Long created,
    String model,
    String object,
    Usage usage
) {

  record Usage(
      Integer completion_tokens,
      Integer prompt_tokens,
      Integer system_tokens,
      Integer total_tokens
  ) {
  }

  public record Choise(
      Answer message
  ) {
    public record Answer(String content) {}
  }
}