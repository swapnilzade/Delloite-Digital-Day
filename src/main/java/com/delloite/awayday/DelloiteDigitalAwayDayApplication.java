package com.delloite.awayday;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DelloiteDigitalAwayDayApplication {

	public static void main(String[] args) {
		final Logger logger = Logger.getLogger(DelloiteDigitalAwayDayApplication.class);
		
		DayAwayEventSchedular dayAwayEventSchedular = new DayAwayEventSchedular();
		try {
			SpringApplication.run(DelloiteDigitalAwayDayApplication.class, args);
			dayAwayEventSchedular.startProcess();
		} catch (Exception ex) {
			logger.error("Error while running Delloite Digital AwayDay Application : " + ex.getMessage());
		}

	}
}
