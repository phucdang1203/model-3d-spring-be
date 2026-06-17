package com.innervix.model3d.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innervix.model3d.common.model.Vector3;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonSupport {

    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public JsonSupport(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String write(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid JSON value", ex);
        }
    }

    public JsonNode readTree(String json, String fallbackJson) {
        try {
            return objectMapper.readTree(json == null || json.isBlank() ? fallbackJson : json);
        } catch (Exception ex) {
            try {
                return objectMapper.readTree(fallbackJson);
            } catch (Exception fallbackEx) {
                throw new IllegalArgumentException("Invalid fallback JSON value", fallbackEx);
            }
        }
    }

    public List<String> readStringList(String json) {
        try {
            return json == null ? List.of() : objectMapper.readValue(json, STRING_LIST);
        } catch (Exception ex) {
            return List.of();
        }
    }

    public Vector3 readVector(String json, Vector3 fallback) {
        try {
            if (json == null) return fallback;
            Double[] values = objectMapper.readValue(json, Double[].class);
            if (values.length != 3) return fallback;
            return new Vector3(values[0], values[1], values[2]);
        } catch (Exception ex) {
            return fallback;
        }
    }

    public String writeVector(Vector3 vector) {
        return write(List.of(vector.x(), vector.y(), vector.z()));
    }
}
