package com.kafka.streams.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IotDevice {
    private UUID id;

    @JsonProperty("serial_number")
    private String serialNumber;

    @JsonProperty("account_sn")
    private String accountSerialNumber;

    public IotDevice() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAccountSerialNumber() {
        return accountSerialNumber;
    }

    public void setAccountSerialNumber(String accountSerialNumber) {
        this.accountSerialNumber = accountSerialNumber;
    }
}