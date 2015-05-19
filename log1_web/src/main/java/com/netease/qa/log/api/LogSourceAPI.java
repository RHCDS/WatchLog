package com.netease.qa.log.api;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.exception.ApiExceptionHandler;
import com.netease.qa.log.invaildrequest.ConflictRequestException;
import com.netease.qa.log.invaildrequest.InvalidRequestException;
import com.netease.qa.log.invaildrequest.NotFoundRequestException;
import com.netease.qa.log.user.service.LogsourceService;
import com.netease.qa.log.util.Match;


@Controller
@RequestMapping("/api/logsource")

public class LogSourceAPI {
	@Resource
	private LogsourceService logsourceService;
	@Resource
	private ApiExceptionHandler apiException;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addLogsource(@RequestParam("logsourcename") String logsourceName,
			@RequestParam("projectid1") String projectid1, @RequestParam("host") String hostname,
			@RequestParam("path") String path, @RequestParam("filePattern") String filePattern,
			@RequestParam("start") String linestart, @RequestParam("keyword") String filterkeyword,
			@RequestParam("regex") String typeregex, @RequestParam("creatorname") String creatorname, Model model)
	      {
		boolean isNum = Match.isInteger(projectid1);
		if(!isNum){
			//projectid 不是num
			InvalidRequestException ex = new InvalidRequestException("projectid  must be a number");
			//400错误，参数错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		//获取post提交的数据
		int projectid = Integer.parseInt(projectid1);
	    int addResult = logsourceService.addLogsource(logsourceName,projectid, hostname, path, filePattern, linestart, filterkeyword, typeregex, creatorname);
		
	    if(addResult == -2){
	    	ConflictRequestException cr = new ConflictRequestException("The logsource is already existence");
	    	//409错误，有冲突
	    	return new ResponseEntity<JSONObject>(apiException.handleConflictRequestException(cr), HttpStatus.CONFLICT);
	    }
	    else if(addResult == -1){
	    	NotFoundRequestException nr = new NotFoundRequestException("Not found");
	    	return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
	    }
	    else if(addResult == 0){
	    	InvalidRequestException ex = new InvalidRequestException("projectid  must is a number");
	    	//内部错误，500,基本很少出现这种错误
	    	return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    else{
	    	JSONObject json = new JSONObject();
	    	json.put("logsourceid", addResult);
	    	return  new ResponseEntity<JSONObject>(json, HttpStatus.OK);
	    }
	    
	    }
	
	@RequestMapping(value = "/{logsourceid1}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> updateLogsource(@PathVariable String logsourceid1, 
			@RequestParam("logsourcename") String logsourcename, @RequestParam("host") String hostname,
			@RequestParam("path") String path, @RequestParam("filePattern") String filePattern,
			@RequestParam("start") String linestart, @RequestParam("keyword") String filterkeyword,
			@RequestParam("regex") String typeregex, Model model){
		boolean isNum = Match.isInteger(logsourceid1);
		if(!isNum){
			//logsourceid 不是数字
			InvalidRequestException ex = new InvalidRequestException("logsourceid  must be a number");
			//400错误，参数错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		int logsourceid = Integer.parseInt(logsourceid1);
		int updateResult = logsourceService.updateLogsource(logsourceid, logsourcename, hostname, path, filePattern, linestart, filterkeyword, typeregex);
		if(updateResult == -1){
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
	    	return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		else if(updateResult == 0){
	    	InvalidRequestException ex = new InvalidRequestException("internal server error");
	    	//内部错误，500,基本很少出现这种错误
	    	return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
		else {
			return  new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/{logsourceid1}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject>  findLogSource(@PathVariable String logsourceid1, Model model){
		//如果logsourceid 小于 0，更改http状态，但是能返回json数据，只是为空而已
		boolean isNum = Match.isInteger(logsourceid1);
		if(!isNum)
		{
			InvalidRequestException ex = new InvalidRequestException("logsourceid  must be a number");
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		int logsourceid = Integer.parseInt(logsourceid1);
		JSONObject logSource = logsourceService.findLogsource(Integer.valueOf(logsourceid));
		if(logSource == null){
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
	    	return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<JSONObject>(logSource, HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/{logsourceid1}", method = RequestMethod.DELETE)
	public ResponseEntity<JSONObject>  deleteLogSource(@PathVariable String logsourceid1,Model model){
		boolean isNum = Match.isInteger(logsourceid1);
		if(!isNum)
		{
			InvalidRequestException ex = new InvalidRequestException("logsourceid  must be a number");
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		int logsourceid = Integer.parseInt(logsourceid1);
		int deleteResult = logsourceService.deleteLogsource(logsourceid);
		if(deleteResult == -1){
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
	    	return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		else if(deleteResult == 0){
	    	InvalidRequestException ex = new InvalidRequestException("internal server error");
	    	//内部错误，500,基本很少出现这种错误
	    	return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
		else {
			return  new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/changestatus/{logsourceid1}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> changeStatus(@PathVariable String logsourceid1, @RequestParam("status") int status, Model model){
		boolean isNum = Match.isInteger(logsourceid1);
		if(!isNum)
		{
			InvalidRequestException ex = new InvalidRequestException("logsourceid  must be a number");
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		int logsourceid = Integer.parseInt(logsourceid1);
		int changeResult = logsourceService.changeLogsourceStatus(logsourceid, status);
		if(changeResult == -1){
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
	    	return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		else if(changeResult == 0){
	    	InvalidRequestException ex = new InvalidRequestException("internal server error");
	    	//内部错误，500,基本很少出现这种错误
	    	return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
		else {
			return  new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		} 
	}
}
