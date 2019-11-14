package com.kafka.streams.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IotUsageRecord {
    private UUID id;

    @JsonProperty("device_sn")
    private String deviceSerialNumber;

    @JsonProperty("download_bytes")
    private long downloadBytes;

    @JsonProperty("upload_bytes")
    private long uploadBytes;

    @JsonProperty("session_start_time")
    private Long sessionStartTimeMs;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public Long getDownloadBytes() {
        return downloadBytes;
    }

    public void setDownloadBytes(Long downloadBytes) {
        this.downloadBytes = downloadBytes;
    }

    public Long getUploadBytes() {
        return uploadBytes;
    }

    public void setUploadBytes(Long uploadBytes) {
        this.uploadBytes = uploadBytes;
    }

    public Long getSessionStartTimeMs() {
        return sessionStartTimeMs;
    }

    public void setSessionStartTimeMs(Long sessionStartTimeMs) {
        this.sessionStartTimeMs = sessionStartTimeMs;
    }
}
