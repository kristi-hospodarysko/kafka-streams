package com.kafka.streams;

import java.time.Duration;
import java.util.Properties;

import com.kafka.streams.data.AggregatedReport;
import com.kafka.streams.data.IotDevice;
import com.kafka.streams.data.IotJoinedRecord;
import com.kafka.streams.data.IotUsageRecord;
import com.kafka.streams.serializer.JsonSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;

public class KafkaStreamsApplication {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-iot-reports");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.AggregatedReportSerge.class);
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
                WallclockTimestampExtractor.class);

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, IotUsageRecord> usageRecords = builder.stream("iot_usage_records", Consumed.with(Serdes.String(), new JsonSerde<>(IotUsageRecord.class)));
        KTable<String, IotDevice> devices = builder.table("iot_devices", Consumed.with(Serdes.String(), new JsonSerde<>(IotDevice.class))); // key == device_sn

        usageRecords
                .selectKey((key, record) -> record.getDeviceSerialNumber())
                .join(devices, (record, device) -> {
                    IotJoinedRecord iotJoinedRecord = new IotJoinedRecord();
                    iotJoinedRecord.setDeviceSerialNumber(device.getSerialNumber());
                    iotJoinedRecord.setAccountSerialNumber(device.getAccountSerialNumber());
                    iotJoinedRecord.setDownloadBytes(record.getDownloadBytes());
                    iotJoinedRecord.setUploadBytes(record.getUploadBytes());
                    return iotJoinedRecord;
                })
                .groupBy((key, record) -> record.getAccountSerialNumber())
                .windowedBy(TimeWindows.of(Duration.ofMinutes(1)))
                .aggregate(AggregatedReport::new, /* initializer */
                        (aggKey, iotJoinedRecord, aggregatedReport) -> aggregatedReport.addBytes(iotJoinedRecord),
                        Materialized.as("iot_aggregated_usage"))
                .mapValues((readOnlyKey, aggregatedReport) -> {
                    aggregatedReport.setPeriodStart(readOnlyKey.window().startTime().toString());
                    aggregatedReport.setPeriodEnd(readOnlyKey.window().endTime().toString());
                    return aggregatedReport;
                });

        final Topology topology = builder.build();

        System.out.println(topology.describe());

        final KafkaStreams streams = new KafkaStreams(topology, props);

        try {
            System.out.println("Starting streams...");
            streams.start();
            System.out.println("Streams started!");
        } catch (Throwable e) {
            System.exit(1);
        }
    }
}
