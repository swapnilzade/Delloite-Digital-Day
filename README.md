# Delloite-Digital-Away-Day

Deloitte Digital is rewarding their employees in recognition for their hard work for range of successful projects by organising a "Deloitte Digital Away Day".
This project helps event organiser to plan events.

# Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.
# Prerequisites
1.	Java 1.8
2.	Maven 3.3
3.	STS (If you want to import code directly to IDE)

# Authors
      Swapnil Zade
# Program Input File
 Duck Herding 60min
Archery 45min
Learning Magic Tricks 40min
Laser Clay Shooting 60min
Human Table Football 30min
Buggy Driving 30min
Salsa & Pickles sprint
2-wheeled Segways 45min
Viking Axe Throwing 60min
Giant Puzzle Dinosaurs 30min
Giant Digital Graffiti 60min
Cricket 2020 60min
Wine Tasting sprint
Arduino Bonanza 30min
Digital Tresure Hunt 60min
Enigma Challenge 45min
Monti Carlo or Bust 60min
New Zealand Haka 30min
Time Tracker sprint
Indiano Drizzle 45min

# Program Output File
Output file will be generated at Delloite-Digital-Away-Day\target\classes\export.xlsx
 

# Execution Steps:
1.	Import project into STS
2.	Run the project.

# Project Structure:
Classes	Purpose
DayAwayEventSchedular.java	 - This is the main class which runs entire process.
ScheduleProcessor.java	     - This Class performs 2 functions to create scheduler
EventDaySchedule.java	       -This Class add events to schedule
AwayDayUtil.java	           -This utility class read input file and write output to Excel
Event.java	                 -Pojo class to store event details
DayAwayConstant.java	        -Project Contstants class
DigitalException.java	Custom Exception class
 
