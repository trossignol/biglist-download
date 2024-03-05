package fr.rossi.biglistdownload.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JsonMapper {

    @Inject
    ObjectMapper mapper;

    public String toJson(Object value) {
        try {
            return this.mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public <T> T fromJson(String json, Class<T> targetClass) {
        try {
            return this.mapper.readValue(json, targetClass);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    private static class JsonException extends RuntimeException {
        public JsonException(Throwable cause) {
            super("Error parsing JSON", cause);
        }
    }
}
