package com.kafka.streams.serializer;

import java.util.Map;

import com.kafka.streams.data.AggregatedReport;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerde<T> implements Serde<T> {
    private Class<T> tClass;

    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    public JsonSerde(Class<T> tClass) {
        this.tClass = tClass;
    }

    public void close() {
    }

    public Serializer<T> serializer() {
        return new JsonSerializer<>();
    }

    public Deserializer<T> deserializer() {
        return new JsonDeserializer<>(tClass);
    }

    public static class AggregatedReportSerge extends JsonSerde<AggregatedReport>{
        public AggregatedReportSerge() {
            super(AggregatedReport.class);
        }
    }
}

