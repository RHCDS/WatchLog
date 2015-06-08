package com.netease.qa.log.web.controller;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.alibaba.fastjson.JSONArray;
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
@RequestMapping(value = "/logsrc")
public class WlogSrcController {

	@Resource
	private ApiExceptionHandler apiException;
	@Resource
	private ProjectService projectService;
	@Resource
	private LogSourceService logSourceService;

	@RequestMapping(value = "/manage/tabledata", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> findLogSourceByProjectid(
			@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "length", required = false) String limit,
			@RequestParam(value = "start", required = false) String offset) {
		if (MathUtil.isEmpty(projectid, limit, offset)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(projectid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(limit) || !MathUtil.isInteger(offset)) {
			InvalidRequestException ex = new InvalidRequestException(Const.LIMIT_AND_OFFSET_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.PROJECT_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		int recordsTotal = logSourceService.getTotalCountByProjectId(Integer.parseInt(projectid));
		String message = Const.RESPONSE_SUCCESSFUL;
		JSONArray data = logSourceService.getLogSourcesListByProjectid(Integer.parseInt(projectid),
				Integer.parseInt(limit), Integer.parseInt(offset));
		if (data == null)
			message = Const.RESPONSE_NOTSUCCESSFUL;
		JSONObject result = new JSONObject();
		result.put("message", message);
		result.put("recordsTotal", recordsTotal);
		result.put("data", data);
		return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "manage/logtable", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> findLogSourceSortedByProjectid(
			@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "sort", required = false, defaultValue = "update_time") String sort,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "offset", required = false) String offset) {
		if (MathUtil.isEmpty(projectid, limit, offset)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(projectid)) {
			InvalidRequestException ex = new InvalidRequestException(Const.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(limit) || !MathUtil.isInteger(offset)) {
			InvalidRequestException ex = new InvalidRequestException(Const.LIMIT_AND_OFFSET_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			NotFoundRequestException nr = new NotFoundRequestException(Const.PROJECT_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		String field = MathUtil.getSortField(sort);
		int recordsTotal = logSourceService.getTotalCountByProjectId(Integer.parseInt(projectid));
		String message = Const.RESPONSE_SUCCESSFUL;
		JSONArray data = logSourceService.getLogSourcesListSortedByProjectid(Integer.parseInt(projectid), field, order,
				Integer.parseInt(limit), Integer.parseInt(offset));
		JSONObject result = new JSONObject();
		if (data == null)
			message = Const.RESPONSE_NOTSUCCESSFUL;
		result.put("message", message);
		result.put("total", recordsTotal);
		result.put("rows", data);
		return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/destroy", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> deleteLogSources(@RequestParam(value = "ids") String ids, Model model) {
		if (MathUtil.isEmpty(ids)) {
			NullParamException ne = new NullParamException(Const.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		// 选中删除，所以日志必存在，不需要进行日志检查
		int[] logsource_ids = MathUtil.parse2IntArray(ids);
		int result = logSourceService.deleteLogSources(logsource_ids);
		if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(Const.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		JSONObject resultJson = new JSONObject();
		resultJson.put("message", Const.RESPONSE_SUCCESSFUL);
		return new ResponseEntity<JSONObject>(resultJson, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getDetailLogsource(@PathVariable String id, Model model) {
		LogSource logSource = logSourceService.getByLogSourceId(Integer.parseInt(id));
		if (logSource == null) {
			model.addAttribute("controller", "WlogManage");
			model.addAttribute("action", "show");
			model.addAttribute("logsrc_name", "NONE");
			model.addAttribute("host_name", "NONE");
			model.addAttribute("logsrc_path", "NONE");
			model.addAttribute("logsrc_file", "NONE");
			model.addAttribute("start_regex", "NONE");
			model.addAttribute("filter_keyword", "NONE");
			model.addAttribute("reg_regex", "NONE");
		} else {
			model.addAttribute("controller", "WlogManage");
			model.addAttribute("action", "show");
			model.addAttribute("logsrc_name", logSource.getLogSourceName());
			model.addAttribute("host_name", logSource.getHostname());
			model.addAttribute("logsrc_path", logSource.getPath());
			model.addAttribute("logsrc_file", logSource.getFilePattern());
			model.addAttribute("start_regex", logSource.getLineStartRegex());
			
			System.out.println("keywords:" + logSource.getLineFilterKeyword());
			
			model.addAttribute("filter_keyword", logSource.getLineFilterKeyword());
			model.addAttribute("reg_regex", logSource.getLineTypeRegex());
		}
		return "logsrc/show";
	}
	
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newLogsrc(Locale locale, Model model) {
		model.addAttribute("controller", "WlogManage" );	
		model.addAttribute("action", "new" );			
		return "logsrc/new";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String addLogsource(
			@RequestParam(value = "logsrc_name", required = false) String logsourceName,
			@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "host_name", required = false) String hostname,
			@RequestParam(value = "logsrc_path", required = false) String path,
			@RequestParam(value = "logsrc_file", required = false) String filepattern,
			@RequestParam(value = "start_regex", required = false) String linestart,
			@RequestParam(value = "filter_keyword_arr[]", required = false) String[] filterkeywords,
			@RequestParam(value = "reg_regex_arr[]", required = false) String[] typeregexs,
			@RequestParam(value = "filter_keyword_con", required = false)String filter_keyword_con,
			@RequestParam(value = "reg_regex_con", required = false)String reg_regex_con,
			@RequestParam(value = "logsourcecreatorname", required = false, defaultValue = "none") String creatorname, RedirectAttributes  model) {
		String ret = "redirect:/logsrc/manage?proj=" + projectid;
		if (MathUtil.isEmpty(logsourceName, projectid, hostname, path, filepattern, linestart, filter_keyword_con,
				reg_regex_con, creatorname)) {
			model.addFlashAttribute("message", Const.NULL_PARAM);
			return ret;
		}
		if(filterkeywords==null || typeregexs == null){
			model.addFlashAttribute("message", Const.NULL_PARAM);
			return ret;
		}
		if (!MathUtil.isInteger(projectid)) {
			model.addFlashAttribute("message", Const.ID_MUST_BE_NUM);
			return ret;
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			model.addFlashAttribute("message", Const.PROJECT_NOT_EXSIT);
			return ret;
		}
		if (logSourceService.checkLogSourceExist(hostname, path, filepattern)) {
			model.addFlashAttribute("message", Const.LOG_ALREADY_EXSIT);
			return ret;
		}
		
		LogSource logSource = new LogSource();
		logSource.setLogSourceName(logsourceName);
		logSource.setProjectId(Integer.parseInt(projectid));
		logSource.setHostname(hostname);
		logSource.setPath(path);
		logSource.setFilePattern(filepattern);
		logSource.setLineStartRegex(linestart);
		logSource.setLineFilterKeyword(MathUtil.parse2Str(filterkeywords, filter_keyword_con));
		logSource.setLineTypeRegex(MathUtil.parse2Str(typeregexs, "OR"));
		logSource.setLogSourceCreatorName(creatorname);
		int result = logSourceService.createLogSource(logSource);
		if (result == 0) {
			model.addFlashAttribute("message", Const.INNER_ERROR);
			return ret; 
		} else {
			model.addFlashAttribute("message", Const.RESPONSE_SUCCESSFUL); 
			return ret;
		}
	}
	
}
