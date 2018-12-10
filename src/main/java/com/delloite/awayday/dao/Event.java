package com.delloite.awayday.dao;

import java.time.LocalTime;

public class Event {

	String strEventName;
	LocalTime eventTime;
	String strEventDuration;

	public String getStrEventName() {
		return strEventName;
	}

	public void setStrEventName(String strEventName) {
		this.strEventName = strEventName;
	}

	public String getStrEventDuration() {
		return strEventDuration;
	}

	public void setStrEventDuration(String strEventDuration) {
		this.strEventDuration = strEventDuration;
	}

	public LocalTime getEventTime() {
		return eventTime;
	}

	public void setEventTime(LocalTime eventTime) {
		this.eventTime = eventTime;
	}

}
