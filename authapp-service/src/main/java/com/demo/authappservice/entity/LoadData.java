package com.demo.authappservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LOADDATA")
public class LoadData {

	@Id	
	private String dataType;
	private String dataValue;

	public LoadData(String dataType, String dataValue) {
		super();
		this.dataType = dataType;
		this.dataValue = dataValue;
	}

	@Override
	public String toString() {
		return "LoadData [dataType=" + dataType + ", dataValue=" + dataValue + "]";
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
}
