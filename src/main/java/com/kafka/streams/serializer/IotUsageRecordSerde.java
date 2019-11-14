package com.kafka.streams.serializer;

import com.kafka.streams.data.IotUsageRecord;

public class IotUsageRecordSerde extends JsonSerde<IotUsageRecord>{
    public IotUsageRecordSerde() {
        super(IotUsageRecord.class);
    }
}
