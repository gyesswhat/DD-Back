package com.example.ddback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class StringArrayListConverter implements AttributeConverter<List<String[]>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String[]> dataList) {
        try {
            return mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String[]> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data, new TypeReference<List<String[]>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
