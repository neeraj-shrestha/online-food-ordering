package com.nepaliCravings.demo.customException;

public class CustomException extends RuntimeException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1592717098043854706L;

	public CustomException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
	public CustomException(String errorMessage) {
		super(errorMessage);
	}
}
