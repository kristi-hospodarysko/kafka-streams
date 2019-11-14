package com.kafka.streams.serializer;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDeserializer<T> implements Deserializer<T> {
    private static Logger LOG = LoggerFactory.getLogger(JsonSerializer.class);
    private final ObjectMapper objectMapper;

    private Class<T> tClass;

    public JsonDeserializer(Class<T> tClass) {
        objectMapper = new ObjectMapper();
        this.tClass = tClass;
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    public T deserialize(String topic, byte[] data) {
        try {
            System.out.println("Deserializing for " + topic);
            return objectMapper.readValue(data, tClass);
        } catch (IOException e) {
            LOG.warn("Deserialization error: " + e.getMessage());
            return null;
        }
    }

    public void close() {

    }
}
