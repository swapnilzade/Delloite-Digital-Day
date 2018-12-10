package com.delloite.awayday;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.delloite.awayday.exception.DigitalException;
import com.delloite.awayday.processor.ScheduleProcessor;
import com.delloite.awayday.util.AwayDayUtil;
import com.delloite.awayday.util.DayAwayConstant;

public class DayAwayEventSchedular {

	final static Logger logger = Logger.getLogger(DayAwayEventSchedular.class);

	static List<EventDaySchedule> schedules = new ArrayList<>();

	/**
	 * This is the staring method which process and create scheduler
	 * @throws Exception
	 * @throws DigitalException
	 */
	public void startProcess() throws Exception, DigitalException {

		logger.info("DayAway processing started!!!!");

		AwayDayUtil awayDayUtil = new AwayDayUtil();
		Map<String, Integer> events = new HashMap<String, Integer>();
		ScheduleProcessor processor = new ScheduleProcessor();
		try {
			events = awayDayUtil.getEventsFromFile(DayAwayConstant.INPUT_FILE);
			if (!events.isEmpty()) {
				Map<String, Integer> SortedTasks = events.entrySet().stream()
						.sorted((Map.Entry.<String, Integer>comparingByValue().reversed())).collect(Collectors
								.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

				schedules = processor.addEventsToScheduler(SortedTasks);
				schedules.stream().forEach(e -> {
					try {
						awayDayUtil.createOutputFile(e.getEvents(), e.getStrTeamName());
					} catch (DigitalException dEx) {
						logger.error("Error while processing DayAwayEvent" + dEx.getMessage());
					}
				});
			} else {
				throw new DigitalException("Empty file");
			}
			logger.info("DayAway process sucessfully completed!!!!");

		} catch (DigitalException dEx) {
			logger.error(
					"Error while processing DayAwayEvent" + dEx.getMessage() + "ERROR Code : " + dEx.getErrorCode());
		} catch (Exception ex) {
			logger.error("Error while processing DayAwayEvent" + ex.getMessage());
		}
	}

}
