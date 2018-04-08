package com.iap.phenologyweather.request.weather;

import java.io.Serializable;

public class CityParams implements Serializable {
	private static final long serialVersionUID = 1L;
	private float latitude;
	private float longitude;
	private String district = "--";
	private String city = "--";
	private String province = "--";
	private long timeLabel;
	private int weatherDataID = 1;
	
	
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public CityParams(){}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public long getTimeLabel() {
		return timeLabel;
	}
	public void setTimeLabel(long timeLabel) {
		this.timeLabel = timeLabel;
	}
	
	public void setWeatherDataID(int weatherDataID){
		this.weatherDataID = weatherDataID;
	}
	public int getWeatherDataID(){
		return weatherDataID;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "CityParams{" +
				"latitude=" + latitude +
				", longitude=" + longitude +
				", district='" + district + '\'' +
				", city='" + city + '\'' +
				", province='" + province + '\'' +
				", timeLabel=" + timeLabel +
				", weatherDataID=" + weatherDataID +
				'}';
	}
}
