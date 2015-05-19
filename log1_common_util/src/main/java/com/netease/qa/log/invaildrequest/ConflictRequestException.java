package com.netease.qa.log.invaildrequest;

public class ConflictRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConflictRequestException(String message){
		super(message);
	}
}
