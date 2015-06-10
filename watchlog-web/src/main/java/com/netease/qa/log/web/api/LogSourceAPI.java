package com.netease.qa.log.web.api;

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
import com.netease.qa.log.exception.ConflictRequestException;
import com.netease.qa.log.exception.InvalidRequestException;
import com.netease.qa.log.exception.NotFoundRequestException;
import com.netease.qa.log.exception.NullParamException;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.service.ProjectService;
import com.netease.qa.log.util.Const;
import com.netease.qa.log.util.MathUtil;

@Controller
@RequestMapping("/api/logsource")
public class LogSourceAPI {

	@Resource
	private LogSourceService logsourceService;

	@Resource
	private ProjectService projectService;

	@Resource
	private ApiExceptionHandler apiException;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<JSONObject> addLogsource(
			@RequestParam(value = "logsourcename", required = false) String logsourceName,
			@RequestParam(value = "projectid", required = false) String projectid,
			@RequestParam(value = "hostname", required = false) String hostname,
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "filepattern", required = false) String filepattern,
			@RequestParam(value = "linestart", required = false) String linestart,
			@RequestParam(value = "filterkeyword", required = false) String filterkeyword,
			@RequestParam(value = "typeregex", required = false) String typeregex,
			@RequestParam(value = "logsourcecreatorname", required = false) String creatorname, Model model) {
		if (MathUtil.isEmpty(logsourceName, projectid, hostname, path, filepattern, linestart, filterkeyword,
				typeregex, creatorname)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isName(logsourceName)) {
			InvalidRequestException ex = new InvalidRequestException(Const.INVALID_NAME);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(projectid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.PROJECT_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		if (logsourceService.checkLogSourceExist(logsourceName)) {
			ConflictRequestException cr = new ConflictRequestException(Const.LOG_NAME_ALREADY_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleConflictRequestException(cr), HttpStatus.CONFLICT);
		}

		if (logsourceService.checkLogSourceExist(hostname, path, filepattern)) {
			ConflictRequestException cr = new ConflictRequestException(Const.LOG_PATH_ALREADY_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleConflictRequestException(cr), HttpStatus.CONFLICT);
		}

		LogSource logSource = new LogSource();
		logSource.setLogSourceName(logsourceName);
		logSource.setProjectId(Integer.parseInt(projectid));
		logSource.setHostname(hostname);
		logSource.setPath(path);
		logSource.setFilePattern(filepattern);
		logSource.setLineStartRegex(linestart);
		logSource.setLineFilterKeyword(filterkeyword);
		logSource.setLineTypeRegex(typeregex);
		logSource.setLogSourceCreatorName(creatorname);
		int result = logsourceService.createLogSource(logSource);
		if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			JSONObject json = new JSONObject();
			json.put("logsourceid", result);
			return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{logsourceid}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> updateLogsource(@PathVariable String logsourceid,
			@RequestParam(value = "logsourcename", required = false) String logsourcename,
			@RequestParam(value = "hostname", required = false) String hostname,
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "filepattern", required = false) String filepattern,
			@RequestParam(value = "linestart", required = false) String linestart,
			@RequestParam(value = "filterkeyword", required = false) String filterkeyword,
			@RequestParam(value = "typeregex", required = false) String typeregex, Model model) {
		if (MathUtil.isEmpty(logsourceid, logsourcename, hostname, path, filepattern, linestart, filterkeyword,
				typeregex)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isName(logsourcename)) {
			InvalidRequestException ex = new InvalidRequestException(Const.INVALID_NAME);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(logsourceid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		LogSource logSource = logsourceService.getByLogSourceId(Integer.parseInt(logsourceid));
		if (logSource == null) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}

		if (!logSource.getLogSourceName().trim().equals(logsourcename)) {
			if (logsourceService.checkLogSourceExist(logsourcename)) {
				ConflictRequestException cr = new ConflictRequestException(Const.LOG_NAME_ALREADY_EXSIT);
				return new ResponseEntity<JSONObject>(apiException.handleConflictRequestException(cr),
						HttpStatus.CONFLICT);
			}
		}
		// 日志源改变了,才去判断是不是其他日志源
		if (!(logSource.getHostname().trim().equals(hostname) && logSource.getPath().trim().equals(path) && logSource
				.getFilePattern().trim().equals(filepattern))) {
			if (logsourceService.checkLogSourceExist(hostname, path, filepattern)) {
				ConflictRequestException cr = new ConflictRequestException(Const.LOG_PATH_ALREADY_EXSIT);
				return new ResponseEntity<JSONObject>(apiException.handleConflictRequestException(cr),
						HttpStatus.CONFLICT);
			}
		}
		logSource.setLogSourceName(logsourcename);
		logSource.setHostname(hostname);
		logSource.setPath(path);
		logSource.setFilePattern(filepattern);
		logSource.setLineStartRegex(linestart);
		logSource.setLineFilterKeyword(filterkeyword);
		logSource.setLineTypeRegex(typeregex);
		int result = logsourceService.updateLogSource(logSource);
		if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{logsourceid}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> findLogSource(@PathVariable String logsourceid, Model model) {
		if (!MathUtil.isInteger(logsourceid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}

		JSONObject logSource = logsourceService.getDetailByLogSourceId(Integer.parseInt(logsourceid));
		if (logSource == null) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<JSONObject>(logSource, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{logsourceid}", method = RequestMethod.DELETE)
	public ResponseEntity<JSONObject> deleteLogSource(@PathVariable String logsourceid, Model model) {
		if (!MathUtil.isInteger(logsourceid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!logsourceService.checkLogSourceExist(Integer.parseInt(logsourceid))) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}

		int result = logsourceService.deleteLogSource(Integer.parseInt(logsourceid));
		if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/changestatus/{logsourceid}", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> changeStatus(@PathVariable String logsourceid,
			@RequestParam(value = "status", required = false) String status, Model model) {
		if (MathUtil.isEmpty(status)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(logsourceid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!status.equals("0") && !status.equals("1") && !status.equals("2")) {
			InvalidRequestException ex = new InvalidRequestException(Const.STATUS_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		LogSource logSource = logsourceService.getByLogSourceId(Integer.parseInt(logsourceid));
		if (logSource == null) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.LOG_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}

		logSource.setLogSourceStatus(Integer.parseInt(status));
		int result = logsourceService.updateLogSource(logSource);
		if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<JSONObject>(new JSONObject(), HttpStatus.OK);
		}
	}

}
