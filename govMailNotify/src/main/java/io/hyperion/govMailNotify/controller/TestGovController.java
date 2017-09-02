package io.hyperion.govMailNotify.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.javafx.collections.MappingChange.Map;

import io.hyperion.govMailNotify.utils.ResponseUtil;
import io.hyperion.govMailNotify.utils.ResultInfo;

/**
 * 
 * @author heliuxing
 *
 */
@RequestMapping("/strategy")
@Controller
public class TestGovController {

	@RequestMapping(value = "/testAILog")
	@ResponseBody
	public ResultInfo testAILog(HttpServletRequest request) {
		try {
			String logs = request.getParameter("logs");
			if(StringUtils.isBlank(logs)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("name", "time out");
			return new ResultInfo(ResponseUtil.success_code, result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	@RequestMapping(value = "/addBlackList")
	@ResponseBody
	public ResultInfo addBlackList(HttpServletRequest request) {
		try {
			String data = request.getParameter("data");
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("name", "time out");
			return new ResultInfo(ResponseUtil.success_code, "已加入黑名单");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	
}
