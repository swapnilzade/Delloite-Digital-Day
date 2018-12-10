package com.delloite.awayday;

import java.time.LocalTime;
import java.util.List;

import org.apache.log4j.Logger;

import com.delloite.awayday.dao.Event;
import com.delloite.awayday.exception.DigitalException;
import com.delloite.awayday.util.AwayDayUtil;

public class EventDaySchedule {

	final static Logger logger = Logger.getLogger(EventDaySchedule.class);

	private LocalTime morningStartTime = AwayDayUtil.startTime;
	private LocalTime EveningStartTime = AwayDayUtil.postLunch;
	private List<Event> events;
	private String strTeamName;
	private int nUsedMorningTime = 0;
	private int nUsedEvningTime = 0;
	private int nTotalTimeAvaiableInMorning = AwayDayUtil.checkTotalTimeAvaiableInMorning();
	private int nTotalTimeAvaiablePostLunch = AwayDayUtil.checkTotalTimeAvaiablePostLunch();
	private boolean isLunchAdded = false;
	private boolean isStaffMotivationAdded = false;

	public EventDaySchedule(List<Event> events, String strTeamName) {

		super();
		this.events = events;
		this.strTeamName = strTeamName;
	}

	/**
	 * This Method will add all morning events
	 * 
	 * @param strEventName
	 * @param nDuration
	 * @return
	 * @throws DigitalException
	 */
	public boolean addMorningEvents(String strEventName, Integer nDuration) throws Exception, DigitalException {
		logger.info("Method addMorningEvents started");
		try {
			if (this.nUsedMorningTime < this.nTotalTimeAvaiableInMorning) {
				Event event = new Event();
				event.setEventTime(this.morningStartTime.plusMinutes(this.nUsedMorningTime));
				event.setStrEventName(strEventName);
				event.setStrEventDuration(nDuration + "min");
				this.events.add(event);
				this.nUsedMorningTime += nDuration;
				return true;
			}
			if (!isLunchAdded && this.nUsedMorningTime == this.nTotalTimeAvaiableInMorning) {
				Event event = new Event();
				event.setEventTime(this.morningStartTime.plusMinutes(this.nUsedMorningTime));
				event.setStrEventName("Lunch Time");
				event.setStrEventDuration(AwayDayUtil.lunchDuration + "min");
				this.events.add(event);
				this.isLunchAdded = true;
			}
		} catch (Exception ex) {
			logger.error("Error while added task to morning events : " + ex.getMessage());
			throw new DigitalException("Error while added task to morning events : " + ex.getMessage(),
					"ERROR_ADD_MORNING_EVENT");
		}
		logger.info("Method addMorningEvents End");
		return false;
	}

	/**
	 * This method will add all evening events
	 * 
	 * @param strEventName
	 * @param nDuration
	 * @return
	 * @throws DigitalException
	 */
	public boolean addEveningEvents(String strEventName, Integer nDuration) throws Exception, DigitalException {

		logger.info("Method addEveningEvents started");
		try {
			if (this.nUsedEvningTime < nTotalTimeAvaiablePostLunch) {
				Event event = new Event();
				event.setEventTime(this.EveningStartTime.plusMinutes(this.nUsedEvningTime));
				event.setStrEventName(strEventName);
				event.setStrEventDuration(nDuration + "min");
				this.events.add(event);
				this.nUsedEvningTime += nDuration;
				return true;
			}
			if (!isStaffMotivationAdded && this.nUsedEvningTime == this.nTotalTimeAvaiablePostLunch) {
				Event event = new Event();
				event.setEventTime(this.EveningStartTime.plusMinutes(this.nUsedEvningTime));
				event.setStrEventName("Staff Motivation Speech");
				this.events.add(event);
				this.isStaffMotivationAdded = true;
			}
		} catch (Exception ex) {
			logger.error("Error while added task to Evening events : " + ex.getMessage());
			throw new DigitalException("Error while added task to Evening events : " + ex.getMessage(),
					"ERROR_ADD_EVENING_EVENT");
		}
		logger.info("Method addEveningEvents End");
		return false;
	}

	public String getStrTeamName() {
		return strTeamName;
	}

	public void setStrTeamName(String strTeamName) {
		this.strTeamName = strTeamName;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

}
