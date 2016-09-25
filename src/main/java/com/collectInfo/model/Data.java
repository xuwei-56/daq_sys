package com.collectInfo.model;

import java.util.Date;

public class Data {
    private String dataId;

    private String deviceIp;

    private Date generateTime;

    private Integer pulseCurrent;

    private Integer pulseAccumulation;

    private Integer voltage;

    private Integer resistanceCurrent;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId == null ? null : dataId.trim();
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp == null ? null : deviceIp.trim();
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    public Integer getPulseCurrent() {
        return pulseCurrent;
    }

    public void setPulseCurrent(Integer pulseCurrent) {
        this.pulseCurrent = pulseCurrent;
    }

    public Integer getPulseAccumulation() {
        return pulseAccumulation;
    }

    public void setPulseAccumulation(Integer pulseAccumulation) {
        this.pulseAccumulation = pulseAccumulation;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    public Integer getResistanceCurrent() {
        return resistanceCurrent;
    }

    public void setResistanceCurrent(Integer resistanceCurrent) {
        this.resistanceCurrent = resistanceCurrent;
    }
}