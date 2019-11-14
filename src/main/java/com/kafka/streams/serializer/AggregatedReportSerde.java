package com.kafka.streams.serializer;

import com.kafka.streams.data.AggregatedReport;

public class AggregatedReportSerde extends JsonSerde<AggregatedReport>{
    public AggregatedReportSerde() {
        super(AggregatedReport.class);
    }
}
