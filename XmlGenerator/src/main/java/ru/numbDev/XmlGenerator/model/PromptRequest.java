/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */

package ru.numbDev.XmlGenerator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PromptRequest(
    String model,
    List<GigachatMessageRequest> messages,
    int n,
    Boolean stream,
    @JsonProperty("max_tokens") Integer maxTokens,
    @JsonProperty("repetition_penalty") Double repetitionPenalty,
    @JsonProperty("update_interval") Integer updateInterval
) {
}