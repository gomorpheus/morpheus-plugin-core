package com.morpheusdata.model;

public class OsType extends MorpheusModel {
	public String code;
	public String category;
	public String platform; //'linux', 'windows', 'esxi', '''
	public String vendor;
	public String name;
	public String osVersion;
	public String osFamily;
	public Integer bitCount = 64;
	public String description;
	public Boolean installAgent = false;
}
