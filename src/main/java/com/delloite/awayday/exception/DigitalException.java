package com.delloite.awayday.exception;

public class DigitalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorCode;
	public DigitalException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DigitalException(String message, Throwable cause) {
		super(message, cause);
	}

	public DigitalException(String message) {
		super(message);
	}
	
	public DigitalException(String message, Throwable cause,String errorCode) {
		super(message,cause);
		this.errorCode =errorCode;
	}
	
	public DigitalException(String message,String errorCode) {
		super(message);
		this.errorCode =errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
