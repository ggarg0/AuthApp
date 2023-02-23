package com.demo.authappservice.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LOADDATA")
public class LoadData {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_loaddata", value = "loaddata_sequence"),
			@Parameter(name = "initial_value", value = "7"), @Parameter(name = "increment_size", value = "1") })
	private int datatypeid;
	private String datatype;
	private String datavalue;

	public LoadData() {
	}

	@Override
	public String toString() {
		return "LoadData [datatypeid=" + datatypeid + ", datatype=" + datatype + ", datavalue=" + datavalue + "]";
	}

	public LoadData(int datatypeid, String datatype, String datavalue) {
		super();
		this.datatypeid = datatypeid;
		this.datatype = datatype;
		this.datavalue = datavalue;
	}

	public int getDatatypeid() {
		return datatypeid;
	}

	public void setDatatypeid(int datatypeid) {
		this.datatypeid = datatypeid;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getDatavalue() {
		return datavalue;
	}

	public void setDatavalue(String datavalue) {
		this.datavalue = datavalue;
	}

	
}
