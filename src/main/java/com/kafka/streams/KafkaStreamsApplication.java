package com.kafka.streams;

import java.time.Duration;
import java.util.Properties;

import com.kafka.streams.data.AggregatedReport;
import com.kafka.streams.data.IotDevice;
import com.kafka.streams.data.IotUsageRecord;
import com.kafka.streams.serializer.AggregatedReportSerde;
import com.kafka.streams.serializer.IotDeviceSerde;
import com.kafka.streams.serializer.IotUsageRecordSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;

public class KafkaStreamsApplication {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-iot-reports-4");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, AggregatedReportSerde.class);
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
                WallclockTimestampExtractor.class);

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, IotUsageRecord> usageRecords = builder.stream("iot_usage_records", Consumed.with(Serdes.String(), new IotUsageRecordSerde()))
                .filter((key, value) -> value != null)
                .selectKey((key, record) -> record.getDeviceSerialNumber())
                .peek((key, value) -> System.out.println(value)); // key == device_sn

        KStream<String, IotDevice> devices = builder.stream("iot_devices", Consumed.with(Serdes.String(), new IotDeviceSerde()))
                .filter((key, value) -> value != null)
                .selectKey((key, device) -> device.getSerialNumber())
                .peek((key, value) -> System.out.println(value)); // key == device_sn

        usageRecords
                .join(devices, (record, device) -> {
                    AggregatedReport aggregatedReport = new AggregatedReport();
                    aggregatedReport.setAccountSerialNumber(device.getAccountSerialNumber());
                    aggregatedReport.setDownloadBytes(record.getDownloadBytes());
                    aggregatedReport.setUploadBytes(record.getUploadBytes());
                    return aggregatedReport;
                    }, JoinWindows.of(Duration.ofMinutes(1)), Joined.with(Serdes.String(), new IotUsageRecordSerde(), new IotDeviceSerde()))
                .filter((key, value) -> value != null)
                .peek((key, value) -> System.out.println(value))
                .groupBy((key, record) -> record.getAccountSerialNumber(), Grouped.with(Serdes.String(), new AggregatedReportSerde()))
                .windowedBy(TimeWindows.of(Duration.ofMinutes(1)))
                .aggregate(AggregatedReport::new, /* initializer */
                        (aggKey, iotJoinedRecord, aggregatedReport) -> aggregatedReport.addBytes(iotJoinedRecord))
                .mapValues((readOnlyKey, aggregatedReport) -> {
                    aggregatedReport.setPeriodStart(readOnlyKey.window().startTime().toString());
                    aggregatedReport.setPeriodEnd(readOnlyKey.window().endTime().toString());
                    return aggregatedReport;
                }).toStream()
                .peek((key, value) -> System.out.println(value))
                .to("iot_aggregated_usage");

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
