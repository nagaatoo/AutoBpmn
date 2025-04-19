package ru.numbDev.XmlGenerator.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// @Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public class ChatResultMapper {

    protected ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // @Named("jsonToServicesResponse")
    // public List<ServicesResponse> mapJsonToServicesResponse(String jsonString) {
    //     try {
    //         return objectMapper.readValue(jsonString, new TypeReference<List<ServicesResponse>>() { });
    //     } catch (JsonProcessingException e) {
    //         throw new RuntimeException("Ошибка при парсинге JSON в Map: " + e.getMessage(), e);
    //     }
    // }
}
