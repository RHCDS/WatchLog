package com.netease.qa.log.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MathUtil {

	public static final boolean isInteger(String str){ 
		//+表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")  
		boolean isNum = str.matches("[0-9]+");   
	    return isNum;
	}
	 
	
	public static Long parseTime(String time) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat(Const.TIME_FORMAT);
		Date date = format.parse(time);
		return date.getTime()/1000;
	}
}
