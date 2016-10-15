package com.collectInfo.model;

public class AlarmThreshold {
	
	//警报阈值数据
	private int pulse_current_MAX;
	private int pulse_current_MIN;
	private int pulse_accumulation_MAX;
	private int pulse_accumulation_MIN;
	private int voltage_MAX;
	private int voltage_MIN;
	private int resistance_current_MAX;
	private int resistance_current_MIN;
	
	
	public int getPulse_current_MAX() {
		return pulse_current_MAX;
	}
	public void setPulse_current_MAX(int pulse_current_MAX) {
		this.pulse_current_MAX = pulse_current_MAX;
	}
	public int getPulse_current_MIN() {
		return pulse_current_MIN;
	}
	public void setPulse_current_MIN(int pulse_current_MIN) {
		this.pulse_current_MIN = pulse_current_MIN;
	}
	public int getPulse_accumulation_MAX() {
		return pulse_accumulation_MAX;
	}
	public void setPulse_accumulation_MAX(int pulse_accumulation_MAX) {
		this.pulse_accumulation_MAX = pulse_accumulation_MAX;
	}
	public int getPulse_accumulation_MIN() {
		return pulse_accumulation_MIN;
	}
	public void setPulse_accumulation_MIN(int pulse_accumulation_MIN) {
		this.pulse_accumulation_MIN = pulse_accumulation_MIN;
	}
	public int getVoltage_MAX() {
		return voltage_MAX;
	}
	public void setVoltage_MAX(int voltage_MAX) {
		this.voltage_MAX = voltage_MAX;
	}
	public int getVoltage_MIN() {
		return voltage_MIN;
	}
	public void setVoltage_MIN(int voltage_MIN) {
		this.voltage_MIN = voltage_MIN;
	}
	public int getResistance_current_MAX() {
		return resistance_current_MAX;
	}
	public void setResistance_current_MAX(int resistance_current_MAX) {
		this.resistance_current_MAX = resistance_current_MAX;
	}
	public int getResistance_current_MIN() {
		return resistance_current_MIN;
	}
	public void setResistance_current_MIN(int resistance_current_MIN) {
		this.resistance_current_MIN = resistance_current_MIN;
	}
	@Override
	public String toString() {
		return "AlarmThreshold [pulse_current_MAX=" + pulse_current_MAX + ", pulse_current_MIN=" + pulse_current_MIN
				+ ", pulse_accumulation_MAX=" + pulse_accumulation_MAX + ", pulse_accumulation_MIN="
				+ pulse_accumulation_MIN + ", voltage_MAX=" + voltage_MAX + ", voltage_MIN=" + voltage_MIN
				+ ", resistance_current_MAX=" + resistance_current_MAX + ", resistance_current_MIN="
				+ resistance_current_MIN + "]";
	}
	
}
