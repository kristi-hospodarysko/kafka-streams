package com.kafka.streams.serializer;

import com.kafka.streams.data.IotDevice;

public class IotDeviceSerde extends JsonSerde<IotDevice>{
    public IotDeviceSerde() {
        super(IotDevice.class);
    }
}
