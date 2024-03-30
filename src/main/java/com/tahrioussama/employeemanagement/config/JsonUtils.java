package com.tahrioussama.employeemanagement.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> jsonStringToMap(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<>() {
            });
        } catch (IOException e) {
            logger.error("Error occurred while converting JSON string to map", e);
            return null;
        }
    }
}