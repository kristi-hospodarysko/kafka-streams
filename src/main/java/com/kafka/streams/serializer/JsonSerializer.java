package com.kafka.streams.serializer;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSerializer<T> implements Serializer<T> {
    private static Logger LOG = LoggerFactory.getLogger(JsonSerializer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    public byte[] serialize(String topic, T data) {
        try {
            System.out.println("Serializing for " + topic);
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            LOG.warn("Serialization error: " + e.getMessage());
            return null;
        }
    }

    public void close() {

    }
}
