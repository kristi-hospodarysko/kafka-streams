package com.kafka.streams.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AggregatedReport {
    @JsonProperty("account_sn")
    private String accountSerialNumber;

    @JsonProperty("period_start")
    private String periodStart;

    @JsonProperty("period_end")
    private String periodEnd;

    @JsonProperty("download_bytes")
    private long downloadBytes;

    @JsonProperty("upload_bytes")
    private long uploadBytes;

    public String getAccountSerialNumber() {
        return accountSerialNumber;
    }

    public void setAccountSerialNumber(String accountSerialNumber) {
        this.accountSerialNumber = accountSerialNumber;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
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

    public AggregatedReport addBytes(AggregatedReport record) {
        this.accountSerialNumber = record.accountSerialNumber;
        this.downloadBytes = this.downloadBytes + record.getDownloadBytes();
        this.uploadBytes = this.uploadBytes + record.getUploadBytes();
        return this;
    }
}
