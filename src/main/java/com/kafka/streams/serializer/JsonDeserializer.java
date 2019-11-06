package com.kafka.streams.serializer;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class JsonDeserializer<T> implements Deserializer<T> {
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
            return objectMapper.readValue(data, tClass);
        } catch (IOException e) {
            throw new SerializationException("Deserialization error: " + e.getMessage(), e);
        }
    }

    public void close() {

    }
}
