package com.netease.qa.log.web.controller;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.exception.ApiExceptionHandler;
import com.netease.qa.log.exception.InvalidRequestException;
import com.netease.qa.log.exception.NotFoundRequestException;
import com.netease.qa.log.exception.NullParamException;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.service.ProjectService;
import com.netease.qa.log.util.ConstCN;
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

	@RequestMapping(value = "manage/logtable", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> findLogSourceSortedByProjectid(
			@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "sort", required = false, defaultValue = "update_time") String sort,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "offset", required = false) String offset) {
		if (MathUtil.isEmpty(projectid, limit, offset)) {
			NullParamException ne = new NullParamException(ConstCN.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(projectid)) {
			InvalidRequestException ex = new InvalidRequestException(ConstCN.ID_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!MathUtil.isInteger(limit) || !MathUtil.isInteger(offset)) {
			InvalidRequestException ex = new InvalidRequestException(ConstCN.LIMIT_AND_OFFSET_MUST_BE_NUM);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex), HttpStatus.BAD_REQUEST);
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			NotFoundRequestException nr = new NotFoundRequestException(ConstCN.PROJECT_NOT_EXSIT);
			return new ResponseEntity<JSONObject>(apiException.handleNotFoundRequestException(nr), HttpStatus.NOT_FOUND);
		}
		String field = MathUtil.getSortField(sort);
		int recordsTotal = logSourceService.getTotalCountByProjectId(Integer.parseInt(projectid));
		String message = ConstCN.RESPONSE_SUCCESSFUL;
		JSONArray data = logSourceService.getLogSourcesListSortedByProjectid(Integer.parseInt(projectid), field, order,
				Integer.parseInt(limit), Integer.parseInt(offset));
		JSONObject result = new JSONObject();
		if (data == null)
			message = ConstCN.RESPONSE_NOTSUCCESSFUL;
		result.put("message", message);
		result.put("total", recordsTotal);
		result.put("rows", data);
		return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/destroy", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> deleteLogSources(@RequestParam(value = "ids") String ids, Model model) {
		if (MathUtil.isEmpty(ids)) {
			NullParamException ne = new NullParamException(ConstCN.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		// 选中删除，所以日志必存在，不需要进行日志检查
		int[] logsource_ids = MathUtil.parse2IntArray(ids);
		int result = logSourceService.deleteLogSources(logsource_ids);
		if (result == 0) {
			InvalidRequestException ex = new InvalidRequestException(ConstCN.INNER_ERROR);
			return new ResponseEntity<JSONObject>(apiException.handleInvalidRequestError(ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		JSONObject resultJson = new JSONObject();
		resultJson.put("message", ConstCN.RESPONSE_SUCCESSFUL);
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
			model.addAttribute("filter_keyword", logSource.getLineFilterKeyword());
			model.addAttribute("reg_regex", logSource.getLineTypeRegex());
		}
		return "logsrc/show";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newLogsrc(Locale locale, Model model) {
		model.addAttribute("controller", "WlogManage");
		model.addAttribute("action", "new");
		return "logsrc/new";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String addLogsource(@RequestParam(value = "logsrc_name", required = false) String logsourceName,
			@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "host_name", required = false) String hostname,
			@RequestParam(value = "logsrc_path", required = false) String path,
			@RequestParam(value = "logsrc_file", required = false) String filepattern,
			@RequestParam(value = "start_regex", required = false) String linestart,
			@RequestParam(value = "filter_keyword_arr[]", required = false) String[] filterkeywords,
			@RequestParam(value = "reg_regex_arr[]", required = false) String[] typeregexs,
			@RequestParam(value = "filter_keyword_con", required = false) String filter_keyword_con,
			@RequestParam(value = "reg_regex_con", required = false) String reg_regex_con,
			@RequestParam(value = "logsourcecreatorname", required = false, defaultValue = "none") String creatorname,
			RedirectAttributes model) {
		String ret = "redirect:/logsrc/manage?proj=" + projectid;
		String ret_new = "redirect:/logsrc/new?proj=" + projectid;
		if (MathUtil.isEmpty(logsourceName, projectid, hostname, path, filepattern, linestart, filter_keyword_con,
				reg_regex_con, creatorname)) {
			model.addFlashAttribute("message", ConstCN.NULL_PARAM);
			return ret_new;
		}
		if (filterkeywords == null || typeregexs == null) {
			model.addFlashAttribute("message", ConstCN.NULL_PARAM);
			return ret_new;
		}
		if (!MathUtil.isInteger(projectid)) {
			model.addFlashAttribute("message", ConstCN.ID_MUST_BE_NUM);
			return ret_new;
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			model.addFlashAttribute("message", ConstCN.PROJECT_NOT_EXSIT);
			return ret_new;
		}
		if (logSourceService.checkLogSourceExist(logsourceName)) {
			model.addFlashAttribute("message", ConstCN.LOG_NAME_ALREADY_EXSIT);
			return ret_new;
		}
		if (logSourceService.checkLogSourceExist(hostname, path, filepattern)) {
			model.addFlashAttribute("message", ConstCN.LOG_PATH_ALREADY_EXSIT);
			return ret_new;
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
			model.addFlashAttribute("message", ConstCN.INNER_ERROR);
			return ret_new;
		} else {
			model.addFlashAttribute("message", ConstCN.RESPONSE_SUCCESSFUL);
			return ret;
		}
	}

	@RequestMapping(value = "/start_monitor", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> startMonitorStatus(@RequestParam(value = "ids", required = false) String ids) {
		if (MathUtil.isEmpty(ids)) {
			NullParamException ne = new NullParamException(ConstCN.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		// 选中修改，所以日志必存在，不需要进行日志检查
		int[] logsource_ids = MathUtil.parse2IntArray(ids);
		int result = logSourceService.changeMonitorStatus(logsource_ids, 1);
		JSONObject resultJson = new JSONObject();
		if (result == -1) {
			resultJson.put("status", -1);
			resultJson.put("message", ConstCN.INNER_ERROR);
			return new ResponseEntity<JSONObject>(resultJson, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			resultJson.put("status", 0);
			resultJson.put("message", ConstCN.RESPONSE_SUCCESSFUL);
			return new ResponseEntity<JSONObject>(resultJson, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/stop_monitor", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> stopMonitorStatus(@RequestParam(value = "ids", required = false) String ids) {
		if (MathUtil.isEmpty(ids)) {
			NullParamException ne = new NullParamException(ConstCN.NULL_PARAM);
			return new ResponseEntity<JSONObject>(apiException.handleNullParamException(ne), HttpStatus.BAD_REQUEST);
		}
		// 选中修改，所以日志必存在，不需要进行日志检查
		int[] logsource_ids = MathUtil.parse2IntArray(ids);
		int result = logSourceService.changeMonitorStatus(logsource_ids, 0);
		JSONObject resultJson = new JSONObject();
		if (result == -1) {
			resultJson.put("status", -1);
			resultJson.put("message", ConstCN.INNER_ERROR);
			return new ResponseEntity<JSONObject>(resultJson, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			resultJson.put("status", 0);
			resultJson.put("message", ConstCN.RESPONSE_SUCCESSFUL);
			return new ResponseEntity<JSONObject>(resultJson, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editLogSource(@PathVariable String logsourceId,
			@RequestParam(value = "proj", required = false) String projectid, Model model) {
		if (MathUtil.isEmpty(logsourceId, projectid) || MathUtil.isInteger(logsourceId)) {
			return "redirect:/logsrc/manage?proj=" + projectid;
		}
		LogSource logSource = logSourceService.getByLogSourceId(Integer.parseInt(logsourceId));

		if (logSource == null) {
			model.addAttribute("controller", "WlogManage");
			model.addAttribute("action", "show");
			model.addAttribute("id", "0");
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
			model.addAttribute("id", logSource.getLogSourceId());
			model.addAttribute("logsrc_name", logSource.getLogSourceName());
			model.addAttribute("host_name", logSource.getHostname());
			model.addAttribute("logsrc_path", logSource.getPath());
			model.addAttribute("logsrc_file", logSource.getFilePattern());
			model.addAttribute("start_regex", logSource.getLineStartRegex());
			model.addAttribute("filter_keyword", logSource.getLineFilterKeyword());
			model.addAttribute("reg_regex", logSource.getLineTypeRegex());
		}
		return "logsrc/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String commitEditLogSource(@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "id", required = false) String logsourceid,
			@RequestParam(value = "logsrc_name", required = false) String logsourceName,
			@RequestParam(value = "host_name", required = false) String hostname,
			@RequestParam(value = "logsrc_path", required = false) String path,
			@RequestParam(value = "logsrc_file", required = false) String filepattern,
			@RequestParam(value = "start_regex", required = false) String linestart,
			@RequestParam(value = "filter_keyword_arr[]", required = false) String[] filterkeywords,
			@RequestParam(value = "reg_regex_arr[]", required = false) String[] typeregexs,
			@RequestParam(value = "filter_keyword_con", required = false) String filter_keyword_con,
			@RequestParam(value = "reg_regex_con", required = false) String reg_regex_con,
			@RequestParam(value = "logsourcecreatorname", required = false, defaultValue = "none") String creatorname,
			RedirectAttributes model) {
		
		String ret_succ = "redirect:/logsrc/manage?proj=" + projectid;
		String ret_fail = "redirect:/logsrc/edit?proj=" + projectid;
		if (MathUtil.isEmpty(logsourceid, logsourceName, projectid, hostname, path, filepattern, linestart, filter_keyword_con,
				reg_regex_con, creatorname)) {
			model.addFlashAttribute("message", ConstCN.NULL_PARAM);
			return ret_fail;
		}
		if (filterkeywords == null || typeregexs == null) {
			model.addFlashAttribute("message", ConstCN.NULL_PARAM);
			return ret_fail;
		}
		if (!MathUtil.isInteger(projectid) || !MathUtil.isInteger(logsourceid)) {
			model.addFlashAttribute("message", ConstCN.ID_MUST_BE_NUM);
			return ret_fail;
		}
		if (!projectService.checkProjectExsit(Integer.parseInt(projectid))) {
			model.addFlashAttribute("message", ConstCN.PROJECT_NOT_EXSIT);
			return ret_fail;
		}
		//日志源名称改了，才能check
		LogSource logsource = logSourceService.getByLogSourceId(Integer.parseInt(logsourceid));
		if(!logsourceName.trim().equals(logsource.getLogSourceName())){
			if(logSourceService.checkLogSourceExist(logsourceName)){
				model.addFlashAttribute("message", ConstCN.LOG_NAME_ALREADY_EXSIT);
				return ret_fail;
			}	
		}
		//path修改
		if(!(hostname.trim().equals(logsource.getHostname()) && path.trim().equals(logsource.getPath()) && filepattern.trim().equals(logsource.getFilePattern()))){
		if (logSourceService.checkLogSourceExist(hostname, path, filepattern)) {
			model.addFlashAttribute("message", ConstCN.LOG_PATH_ALREADY_EXSIT);
			return ret_fail;
		}
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
		int result = logSourceService.updateLogSource(logSource);
		if (result == 0) {
			model.addFlashAttribute("message", ConstCN.INNER_ERROR);
			return ret_fail;
		} else {
			model.addFlashAttribute("message", ConstCN.RESPONSE_SUCCESSFUL);
			return ret_succ;
		}

	}

}
