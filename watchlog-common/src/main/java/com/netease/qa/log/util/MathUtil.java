package com.netease.qa.log.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MathUtil {

	public static final boolean isInteger(String str){ 
		//+表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")  
		boolean isNum = str.matches("[0-9]+");   
	    return isNum;
	}
   
	public static final boolean isEng(String str){
		//只含有数字、字母、下划线,并且不能以下划线开头和结尾
		boolean isEng = str.matches("^(?!_)(?!.*?_$)[0-9a-zA-Z_]+$");
		return isEng;
	}
	
	public static Long parse2Long(String time) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat(Const.TIME_FORMAT);
		Date date = format.parse(time);
		return date.getTime()/1000;
	}
	
	
	public static String parse2Str(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(Const.TIME_FORMAT);
		return sdf.format(new Date(time * 1000));
	}
	
	
	public static String parse2Str(Timestamp time){
		SimpleDateFormat sdf = new SimpleDateFormat(Const.TIME_FORMAT);
	    return sdf.format(time);
	}
	
	
	public static boolean isEmpty(String... params){
		for(String param : params){
			if(param == null || param.trim().equals("")){
				return true; 
			}
		}
		return false;
	}
	
	
	public static String getSortField(String string){
		String str = string.trim();
		if(str.equals("update_time"))
			return "modify_time";
		if(str.equals("id"))
			return "log_source_id";
		if(str.equals("logsrc_name"))
			return "log_source_name";
		if(str.equals("host_name"))
			return "hostname";
		if(str.equals("logsrc_path"))
			return "path";
		if(str.equals("logsrc_file"))
			return "";
		if(str.equals("status"))
			return "log_source_status";
		if(str.equals("creator"))
			return "log_source_creator_name";
		else
			return "modify_time";
	}
	
}
