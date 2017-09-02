package io.hyperion.govMailNotify.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.javafx.collections.MappingChange.Map;

import io.hyperion.govMailNotify.utils.ItsmRequest;
import io.hyperion.govMailNotify.utils.ResponseUtil;
import io.hyperion.govMailNotify.utils.ResultInfo;

/**
 * 
 * @author heliuxing
 *
 */
@RequestMapping("/strategy")
@Controller
public class SmartFixController {
	
	private final Logger logger = Logger.getLogger(getClass());	

	@RequestMapping(value = "/smartFix")
	@ResponseBody
	public ResultInfo smartFix(HttpServletRequest request) {
		try {
			String dataAnalysisResult = request.getParameter("dataAnalysisResult");
			if(StringUtils.isBlank(dataAnalysisResult)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			String result = null;
			if (dataAnalysisResult.equals("esbconsumer")) {
				if(ItsmRequest.handle()) {
					result = "重启esb-consumer成功";
				} else {
					result = "重启esb-consumer失败";
				}
			} else {
				result = "发生" + dataAnalysisResult + "问题，请重启相关模块";
			}

			logger.info("smartFix done! exptype=" + dataAnalysisResult + ", govResult=" + result);
			return new ResultInfo(ResponseUtil.success_code, result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
}
