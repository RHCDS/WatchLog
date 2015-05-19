package com.netease.qa.log.invaildrequest;

public class NotFoundRequestException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundRequestException(String message){
		super(message);
	}
}
