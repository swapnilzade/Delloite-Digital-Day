package com.delloite.awayday.util;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.delloite.awayday.dao.Event;
import com.delloite.awayday.exception.DigitalException;

public class AwayDayUtil {
	final static Logger logger = Logger.getLogger(AwayDayUtil.class);

	public static LocalTime startTime = LocalTime.of(9, 00);
	public static LocalTime LunchTime = LocalTime.of(12, 00);
	public static LocalTime postLunch = LocalTime.of(13, 00);
	public static LocalTime endTime = LocalTime.of(17, 00);
	public static Integer lunchDuration = 60;
	public static long availableMonrigTime;

	public static int dayTimeAvailable() {
		long dDayMin = MINUTES.between(startTime, endTime) - lunchDuration;
		return (int) dDayMin;
	}

	public static int checkTimeAvaiable() {
		long dDayMin = MINUTES.between(startTime, endTime);
		return (int) dDayMin;
	}

	public static int checkHowManyTeamsRequired(int totalEventTime, int dayTimeAvaiable) {
		int nTeamSize = 0;
		// need to work on this
		double extraDay = ((double)totalEventTime / (double)dayTimeAvaiable);
		nTeamSize += (Math.ceil(extraDay));
		return nTeamSize;
	}

	public static int checkTotalTimeAvaiablePostLunch() {
		Long availableTime = MINUTES.between(postLunch, endTime);
		return availableTime.intValue();

	}

	public static int checkTotalTimeAvaiableInMorning() {
		Long availableTime = MINUTES.between(startTime, LunchTime);
		return availableTime.intValue();
	}

	/**
	 * This method will process the input file and add events to HashMap
	 * 
	 * @param inputFile
	 * @return hashMap of Events
	 * @throws Exception
	 */
	public Map<String, Integer> getEventsFromFile(String inputFile) throws Exception {
		logger.info("Method getEventsFromFile() Started");
		Map<String, Integer> map = new HashMap<String, Integer>();
		InputStream inputStream=null;
		InputStreamReader inputStreamReader=null;
		BufferedReader br=null;
		try {
			 inputStream = this.getClass().getClassLoader().getResourceAsStream(inputFile);
		     inputStreamReader = new InputStreamReader(inputStream);
		     br = new BufferedReader(inputStreamReader);
		    String line;
		    while ((line = br.readLine()) != null) 
		    {
		   	 	if (Pattern.matches(DayAwayConstant.REGEX, line)) {
					Integer duration = getMinutes(line.substring(line.lastIndexOf(" ") + 1));
					map.put(line.substring(0, line.lastIndexOf(" ")), duration);
				}
			}
		    logger.info("Method getEventsFromFile() End.");
		} catch (DigitalException dEx) {
			logger.error(
					"Error while reading file from inputFile" + dEx.getMessage() + "ErrorCode: " + dEx.getErrorCode());
			throw dEx;
		} catch (Exception ex) {
			throw new DigitalException("Error while reading file from inputFile" + ex.getMessage(), "ERROR_INPUT_FILE");
		}finally{
			br.close();
			inputStreamReader.close();
			inputStream.close();
		}
		
		
		return map;
	}

	/**
	 * Converts minutes into Integer.
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private static Integer getMinutes(String value) throws Exception {
		String SPRINT = DayAwayConstant.SPRINT;
		Integer result = 0;
		if (SPRINT.equals(value))
			result = 15;
		else
			result = Integer.valueOf(value.substring(0, value.length() - 3));

		if (result <= 0 || result > 60) {
			throw new DigitalException("Invalid task duration: " + result, "ERROR_GET_MINUTES");
		}
		
		return result;
	}

	public Object createOutputFile(List<Event> events, String strTeamName) throws DigitalException {
		logger.info("Method createOutputFile() Started");

		File file = null;
		XSSFWorkbook workbook = null;
		OutputStream fos = null;
		try {
			Sheet sheet = null;
			file = Paths.get(AwayDayUtil.class.getClassLoader().getResource(DayAwayConstant.OUTPUT_FILE).toURI())
					.toFile();
			if (file.exists()) {
				workbook = (XSSFWorkbook) WorkbookFactory.create(new FileInputStream(file));
				
			} else {
				workbook = new XSSFWorkbook();
			}
			if (null != workbook.getSheet(strTeamName)) {
				sheet = workbook.getSheet(strTeamName);
			} else
				sheet = workbook.createSheet(strTeamName);
			List<String> fieldNames = getFieldNamesForClass(events.get(0).getClass());
			int rowCount = 0;
			int columnCount = 0;
			Row row = sheet.createRow(rowCount++);
			for (String fieldName : fieldNames) {
				Cell cell = row.createCell(columnCount++);
				cell.setCellValue(fieldName);
			}
			Class<? extends Object> classz = events.get(0).getClass();
			for (Event t : events) {
				row = sheet.createRow(rowCount++);
				columnCount = 0;
				for (String fieldName : fieldNames) {
					Cell cell = row.createCell(columnCount);
					Method method = null;
					try {
						method = classz.getMethod("get" + capitalize(fieldName));
					} catch (NoSuchMethodException nme) {
						method = classz.getMethod("get" + fieldName);
					}
					Object value = method.invoke(t, (Object[]) null);
					if (value != null) {
						if (value instanceof String) {
							cell.setCellValue((String) value);
						} else if (value instanceof Long) {
							cell.setCellValue((Long) value);
						} else if (value instanceof Integer) {
							cell.setCellValue((Integer) value);
						} else if (value instanceof Double) {
							cell.setCellValue((Double) value);
						} else if (value instanceof LocalTime) {
							cell.setCellValue((String) value.toString());
						}
					}
					columnCount++;
				}
			}
			fos = new FileOutputStream(file);
			workbook.write(fos);
			fos.flush();
			logger.info("Method createOutputFile() End");
		}catch (ArithmeticException e) {
	       
	    } 
		catch (Exception ex) {
			throw new DigitalException("Error while creating output file" + ex.getMessage(), "ERROR_OUTPUT_FILE");
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
			}
		}
		return workbook;

	}

	// retrieve field names from a POJO class
	private static List<String> getFieldNamesForClass(Class<?> eventClass) {
		List<String> fieldNames = new ArrayList<String>();
		Field[] fields = eventClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fieldNames.add(fields[i].getName());
		}
		return fieldNames;
	}

	// capitalize the first letter of the field name for retriving value of the
	// field later
	private static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);

	}

}
