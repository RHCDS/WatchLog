package com.netease.qa.log.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MathUtil {

	public static final boolean isInteger(String str) {
		// +表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")
		boolean isNum = str.matches("[0-9]+");
		return isNum;
	}

	public static final boolean isEng(String str) {
		// 只含有数字、字母、下划线,并且不能以下划线开头和结尾
		boolean isEng = str.matches("^(?!_)(?!.*?_$)[0-9a-zA-Z_]+$");
		return isEng;
	}
	
	public static final boolean isName(String str){
		//只含有汉字、数字、字母、下划线,并且不能以下划线开头和结尾,长度1-20之间
		boolean isName = str.matches("^(?!_)(?!.*?_$)[0-9a-zA-Z_\u4e00-\u9fa5]{1,100}$");
		return isName;
	}

	public static Long parse2Long(String time) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(Const.TIME_FORMAT);
		Date date = format.parse(time);
		return date.getTime() / 1000;
	}

	public static String parse2Str(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(Const.TIME_FORMAT);
		return sdf.format(new Date(time * 1000));
	}
	
	public static String parse2Str(Date time){
		SimpleDateFormat sdf = new SimpleDateFormat(Const.TIME_FORMAT);
		return sdf.format(time);
	}

	public static String parse2Str(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat(Const.TIME_FORMAT);
		return sdf.format(time);
	}
	
	public static Timestamp parse2Time(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat(Const.TIME_FORMAT);
		Timestamp time = null;
		try {
			Date date = sdf.parse(str);
			time = new Timestamp(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	public static boolean isEmpty(String... params) {
		for (String param : params) {
			if (param == null || param.trim().equals("")) {
				return true;
			}
		}
		return false;
	}

	public static String getSortField(String string) {
		String str = string.trim();
		if (str.equals("update_time"))
			return "modify_time";
		if (str.equals("id"))
			return "log_source_id";
		if (str.equals("logsrc_name"))
			return "log_source_name";
		if (str.equals("host_name"))
			return "hostname";
		if (str.equals("logsrc_path"))
			return "path";
		if (str.equals("logsrc_file"))
			return "file_pattern";
		if (str.equals("status"))
			return "status";
		if (str.equals("creator"))
			return "creator_id";
		else
			return "modify_time";
	}
	
	public static String getReportField(String str){
		String str0 = str.trim();
		if(str0.equals("start_time"))
			return "start_time";
		if(str0.equals("end_time"))
			return "end_time";
		if(str0.equals("create_time"))
			return "create_time";
		if(str0.equals("title"))
			return "title";
		else
			return "create_time";
	}

	public static int[] parse2IntArray(String str) {
		String[] strs = str.split(",");
		int[] nums = new int[strs.length];
		for (int i = 0; i < strs.length; i++) {
			nums[i] = Integer.parseInt(strs[i].trim());
		}
		return nums;
	}

	public static String parse2Str(String[] strs, String con) {
		ArrayList<String> newStrs =  new ArrayList<String>();
		for(int i = 0; i < strs.length; i++){
			if(!strs[i].trim().isEmpty())
			newStrs.add(strs[i].trim());
		}
		if(newStrs.size() == 0)
		{
			if(con.equals(Const.FILITER_TYPE))
				return " ";
			else
			return "NONE";
		}
		StringBuffer sb = new StringBuffer();
		if (con.trim().toLowerCase().equals("and")) {
			for (int i = 0; i < newStrs.size() - 1; i++) {
					sb.append(newStrs.get(i)).append(Const.FILTER_KEYWORD_AND);
			}
		} else {
			for (int i = 0; i < newStrs.size() - 1; i++) {
					sb.append(newStrs.get(i)).append(Const.FILTER_KEYWORD_OR);
			}
			
		}
		sb.append(newStrs.get(newStrs.size()-1));
		return sb.toString();
	}

}
