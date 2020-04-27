package com.kthw.pmis.model;

//Table: sequence, Column: 3
public class Sequence {
	String name; // name VARCHAR
	int current_value; // current_value INT
	int increment; // increment INT

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setCurrent_value(int current_value) {
		this.current_value = current_value;
	}

	public int getCurrent_value() {
		return this.current_value;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public int getIncrement() {
		return this.increment;
	}

}