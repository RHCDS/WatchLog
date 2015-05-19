package com.netease.qa.log.util;

public class Match {

	public static final boolean isInteger(String str){ 
		//+表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")  
		boolean isNum = str.matches("[0-9]+");   
	    return isNum;
	}
}
