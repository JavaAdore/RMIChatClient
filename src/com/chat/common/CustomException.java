package com.chat.common;

public class CustomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomException()
	{
		
	}
	
	public CustomException(String message)
	{
		super(message);
	}
}
