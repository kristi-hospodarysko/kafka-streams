package com.kafka.streams.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IotJoinedRecord {
    @JsonProperty("device_sn")
    private String deviceSerialNumber;

    @JsonProperty("account_sn")
    private String accountSerialNumber;

    @JsonProperty("download_bytes")
    private Long downloadBytes;

    @JsonProperty("upload_bytes")
    private Long uploadBytes;

    @JsonProperty("session_start_time")
    private Long sessionStartTimeMs;

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getAccountSerialNumber() {
        return accountSerialNumber;
    }

    public void setAccountSerialNumber(String accountSerialNumber) {
        this.accountSerialNumber = accountSerialNumber;
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
