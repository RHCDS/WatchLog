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
			@RequestParam("projectid") String projectid, @RequestParam("hostname") String hostname,
			@RequestParam("path") String path, @RequestParam("filepattern") String filepattern,
			@RequestParam("linestart") String linestart, @RequestParam("filterkeyword") String filterkeyword,
			@RequestParam("typeregex") String typeregex, @RequestParam("logsourcecreatorname") String creatorname, Model model) {
		boolean isNum = Match.isInteger(projectid);
		if (!isNum) {
			// projectid 不是num
			InvalidRequestException ex = new InvalidRequestException("projectid  must be a number");
			// 400错误，参数错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		// 获取post提交的数据
		int addResult = logsourceService.addLogsource(logsourceName, Integer.parseInt(projectid), hostname, path, filepattern, linestart,
				filterkeyword, typeregex, creatorname);

		if (addResult == -2) {
			ConflictRequestException cr = new ConflictRequestException("The logsource is already existence");
			// 409错误，有冲突
			return new ResponseEntity<JSONObject>(apiException.handleConflictRequestException(cr), HttpStatus.CONFLICT);
		} else if (addResult == -1) {
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} else if (addResult == 0) {
			InvalidRequestException ex = new InvalidRequestException("projectid  must is a number");
			// 内部错误，500,基本很少出现这种错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			JSONObject json = new JSONObject();
			json.put("logsourceid", addResult);
			return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/{logsourceid}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> updateLogsource(@PathVariable String logsourceid,
			@RequestParam("logsourcename") String logsourcename, @RequestParam("hostname") String hostname,
			@RequestParam("path") String path, @RequestParam("filepattern") String filepattern,
			@RequestParam("linestart") String linestart, @RequestParam("filterkeyword") String filterkeyword,
			@RequestParam("typeregex") String typeregex, Model model) {
		boolean isNum = Match.isInteger(logsourceid);
		if (!isNum) {
			// logsourceid 不是数字
			InvalidRequestException ex = new InvalidRequestException("logsourceid  must be a number");
			// 400错误，参数错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		int updateResult = logsourceService.updateLogsource(Integer.parseInt(logsourceid), logsourcename, hostname, path, filepattern,
				linestart, filterkeyword, typeregex);
		if (updateResult == -1) {
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} else if (updateResult == 0) {
			InvalidRequestException ex = new InvalidRequestException("internal server error");
			// 内部错误，500,基本很少出现这种错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{logsourceid}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> findLogSource(@PathVariable String logsourceid, Model model) {
		// 如果logsourceid 小于 0，更改http状态，但是能返回json数据，只是为空而已
		boolean isNum = Match.isInteger(logsourceid);
		if (!isNum) {
			InvalidRequestException ex = new InvalidRequestException("logsourceid  must be a number");
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		JSONObject logSource = logsourceService.findLogsource(Integer.parseInt(logsourceid));
		if (logSource == null) {
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<JSONObject>(logSource, HttpStatus.OK);
	}

	@RequestMapping(value = "/{logsourceid}", method = RequestMethod.DELETE)
	public ResponseEntity<JSONObject> deleteLogSource(@PathVariable String logsourceid, Model model) {
		boolean isNum = Match.isInteger(logsourceid);
		if (!isNum) {
			InvalidRequestException ex = new InvalidRequestException("logsourceid must be a number");
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		int deleteResult = logsourceService.deleteLogsource(Integer.parseInt(logsourceid));
		if (deleteResult == -1) {
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} else if (deleteResult == 0) {
			InvalidRequestException ex = new InvalidRequestException("internal server error");
			// 内部错误，500,基本很少出现这种错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/changestatus/{logsourceid}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> changeStatus(@PathVariable String logsourceid,
			@RequestParam("status") String status, Model model) {
		if (!Match.isInteger(logsourceid) || !Match.isInteger(status)) {
			InvalidRequestException ex = new InvalidRequestException("logsourceid or status  must be a number");
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		int changeResult = logsourceService.changeLogsourceStatus(Integer.parseInt(logsourceid), Integer.parseInt(status));
		if (changeResult == -1) {
			NotFoundRequestException nr = new NotFoundRequestException("Not found");
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} else if (changeResult == 0) {
			InvalidRequestException ex = new InvalidRequestException("internal server error");
			// 内部错误，500,基本很少出现这种错误
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}
}
