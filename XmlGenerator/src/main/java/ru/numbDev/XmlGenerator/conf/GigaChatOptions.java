package ru.numbDev.XmlGenerator.conf;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.ChatOptions;

@RequiredArgsConstructor
public class GigaChatOptions implements ChatOptions {

  private final GigaChatProperties gigaChatProperties;

  @Override
  public String getModel() {
    return gigaChatProperties.getModel();
  }

  @Override
  public Double getFrequencyPenalty() {
    return gigaChatProperties.getFrequencyPenalty();
  }

  @Override
  public Integer getMaxTokens() {
    return gigaChatProperties.getMaxTokens();
  }

  @Override
  public Double getPresencePenalty() {
    return gigaChatProperties.getPresencePenalty();
  }

  @Override
  public List<String> getStopSequences() {
    return gigaChatProperties.getStopSequences();
  }

  @Override
  public Double getTemperature() {
    return gigaChatProperties.getTemperature();
  }

  @Override
  public Integer getTopK() {
    return gigaChatProperties.getTopK();
  }

  @Override
  public Double getTopP() {
    return gigaChatProperties.getTopP();
  }

  @Override
  public ChatOptions copy() {
    return new GigaChatOptions(gigaChatProperties);
  }
}