package com.kthw.pmis.model.system;

import java.io.Serializable;

public class KtCalendar implements Serializable {

	int id;
  String title;
  String start;
  String end;
  int allDay;
  String color;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getStart() {
	return start;
}
public void setStart(String start) {
	this.start = start;
}
public String getEnd() {
	return end;
}
public void setEnd(String end) {
	this.end = end;
}
public int getAllDay() {
	return allDay;
}
public void setAllDay(int allDay) {
	this.allDay = allDay;
}
public String getColor() {
	return color;
}
public void setColor(String color) {
	this.color = color;
}


}
