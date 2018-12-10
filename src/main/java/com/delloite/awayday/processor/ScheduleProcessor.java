package com.delloite.awayday.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.delloite.awayday.EventDaySchedule;
import com.delloite.awayday.dao.Event;
import com.delloite.awayday.exception.DigitalException;
import com.delloite.awayday.util.AwayDayUtil;

public class ScheduleProcessor {
	final static Logger logger = Logger.getLogger(ScheduleProcessor.class);
	List<EventDaySchedule> EventDaySchedules = new ArrayList<>();

	/**
	 * This is the main method which creates scheduler for the events.
	 * 
	 * @param events
	 * @return Schedule event on the basis of teams.
	 * @throws Exception
	 */
	public List<EventDaySchedule> addEventsToScheduler(Map<String, Integer> events) throws Exception, DigitalException {
		logger.info("Method addEventsToScheduler() Started");

		List<EventDaySchedule> schedules = new ArrayList<>();
		try {
			int nTotalDayTimeAvaiable = AwayDayUtil.dayTimeAvailable();

			int nTotalEventTime = events.values().stream().mapToInt(Integer::intValue).sum();

			int nTeamSize = AwayDayUtil.checkHowManyTeamsRequired(nTotalEventTime, nTotalDayTimeAvaiable);

			schedules = createSchedule(events, nTeamSize);
		} catch (DigitalException dEx) {
			throw dEx;
		} catch (Exception ex) {
			throw new DigitalException("Error while processing Event Scheduler" + ex.getMessage(),
					"ERROR_EVENT_SCHEDULER");
		}
		logger.info("Method addEventsToScheduler() End");
		return schedules;

	}

	/**
	 * This method will create schedule
	 * 
	 * @param events
	 * @param teamSize
	 * @return
	 * @throws DigitalException
	 * @throws Exception
	 */
	public List<EventDaySchedule> createSchedule(Map<String, Integer> events, int teamSize)
			throws Exception, DigitalException {

		logger.info("Method createSchedule() Started");
		try {
			for (int i = 1; i <= teamSize; i++) {
				EventDaySchedules.add(new EventDaySchedule(new ArrayList<Event>(), "Team" + i));
			}
			events.entrySet().stream().forEach(e -> {
				try {
					addEvent(e.getKey(), e.getValue());
				} catch (Exception e1) {
					logger.error("Error whiel creating schedule");
				}
			});
		} catch (Exception ex) {
			throw ex;
		}
		logger.info("Method createSchedule() End");
		return EventDaySchedules;

	}

	/**
	 * This method will add event in scheduler
	 * 
	 * @param strEventName
	 * @param nDuration
	 * @return
	 * @throws Exception
	 * @throws DigitalException
	 */
	private void addEvent(String strEventName, Integer nDuration) throws Exception, DigitalException {

		logger.info("Method addEvent() Started");
		try {
			boolean isTaskAdded = false;
			Iterator<EventDaySchedule> iterator;
			iterator = EventDaySchedules.iterator();
			while (!isTaskAdded && iterator.hasNext()) {
				isTaskAdded = ((EventDaySchedule) iterator.next()).addMorningEvents(strEventName, nDuration);
			}

			iterator = EventDaySchedules.iterator();
			while (!isTaskAdded && iterator.hasNext()) {
				isTaskAdded = ((EventDaySchedule) iterator.next()).addEveningEvents(strEventName, nDuration);
			}
			logger.info("Method addEvent() End");
		} catch (DigitalException dEx) {
			throw dEx;
		} catch (Exception ex) {
			logger.error("Error while adding event" + ex.getMessage());
			throw new DigitalException("Error while adding event" + ex.getMessage(), "ERROR_ADD_EVENT");
		}

	}
}
