package com.netease.qa.log.invaildrequest;


public class InvalidRequestException extends RuntimeException  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String message){
		super(message);
	}
}
