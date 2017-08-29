package io.hyperion.managerPlatform.controller;
//package org.hyperion.managerPlatform.controller;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.pingan.fmm.common.ResponseEnum;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.pingan.fmm.common.CommonController;
//import com.pingan.fsg.webii.dao.KnowledgeStrategyBaseInfoDAO;
//import com.pingan.fsg.webii.dao.KnowledgeStrategyConfigDAO;
//import com.pingan.fsg.webii.dao.KnowledgeStrategyStatDAO;
//import com.pingan.fsg.webii.dto.KnowledgeStrategyConfigDTO;
//import com.pingan.fsg.webii.dto.KnowledgeStrategyStatDTO;
//import com.pingan.fsg.webii.factory.KnowledgeStrategy;
//import com.pingan.fsg.webii.factory.KnowledgeStrategyFactory;
//import com.pingan.fsg.webii.form.StrategyCallForm;
//import com.pingan.fsg.webii.redis.KnowledgeRedis;
//import com.pingan.fsg.webii.service.KnowledgeStrategyService;
//import com.pingan.fsg.webii.task.ScanRedis;
//import com.pingan.fsg.webii.task.SynStrategyBaseInfo;
//
//
///**
// * 
// * @author heliuxing
// *
// */
//@Controller
//@RequestMapping("/fsg/test")
//public class TestController extends CommonController {
//	
//	private final static String TEST_KEY = "test-test";
//	
//	@Autowired
//	private KnowledgeStrategyService strategy;
//	
//	@Autowired
//	private ScanRedis scanRedis;
//	
//	@Autowired
//	private SynStrategyBaseInfo synStrategyBaseInfo;
//	
//	@Autowired
//	private KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO;
//	
//	@Autowired
//	private KnowledgeStrategyStatDAO knowledgeStrategyStatDAO;
//	
//	@Autowired
//	private KnowledgeStrategyBaseInfoDAO KnowledgeStrategyBaseInfoDAO;
//
//	@Value("${strategy.call.uri}")
//	private String strategyCallUri;
//	
//	@Resource(name = "knowledge_float_redis")
//	private KnowledgeRedis<Float> knowledgeRedis;
//	
//
//	@RequestMapping(value = "/testStrategyByHttp", method = RequestMethod.POST)
//	@ResponseBody
//	public ModelMap testStrategyByHttp(HttpServletRequest request,
//		@ModelAttribute StrategyCallForm form) throws IOException {
//		ModelMap model = new ModelMap();
//		PrintWriter out = null;
//        BufferedReader in = null;
//		try {
//            String url = "http://" + request.getHeader("host") + strategyCallUri;
//			String result = "";
//			URL realUrl = new URL(url);
//            // 打开和URL之间的连接
//            URLConnection conn = realUrl.openConnection();
//            // 设置通用的请求属性
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // 发送请求参数
//            String params = "method="+form.getMethod()+"&key="+form.getKey()+"&field="+form.getField() + 
//            	"&value=" + String.valueOf(form.getValue());
//            //String params = "method=inc&key=crashMonitor&field=PAFFRoot&value=1";
//            out.print(params);
//            // flush输出流的缓冲
//            out.flush();
//            // 定义BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//                //System.out.println(line);
//            }
////            System.out.println("================");
////            System.out.println(result);
//		} catch(Exception e) {
//			logger.error("exec command fail");
//			e.printStackTrace();
//			buildResponse(model, ResponseEnum.FAILURE);
//		} finally {
//			in.close();
//			
//		}
//		return model;
//	}
//	
//	@RequestMapping(value = "/testStrategyByAnnotation", method = RequestMethod.POST)
//	@ResponseBody
//	public ModelMap testStrategyByAnnotation(HttpServletRequest request,
//		String dataType, String appId, String os, String appVersion, Map<String, Object> data) {
//		ModelMap model = new ModelMap();
//		try {
//            boolean result = strategy.call(dataType, appId, os, appVersion, data);
//			if (result) {	
//				buildResponse(model, ResponseEnum.SUCCESS);
//			} else {
//				buildResponse(model, ResponseEnum.FAILURE);
//			}
//		} catch (Exception e) {
//			buildResponse(model, ResponseEnum.FAILURE);
//		}
//		return model;
//	}
//	
//	/**
//	 * 用来验证应用策略是否能够被正常调用，对参数在redis上进行加1
//	 * @param request
//	 */
//	@RequestMapping(value = "/testAppStrategyCallback", method = RequestMethod.GET)
//	@ResponseBody
//	public void testAppStrategyCallback(HttpServletRequest request) {
//		byte keyByte[] = TEST_KEY.getBytes();
//		byte fieldByte[] = null;
//		if (request.getParameter("key") != null) {
//			fieldByte = ("key-" + request.getParameter("key")).getBytes();
//			knowledgeRedis.hincrByFloat(keyByte, fieldByte, 1);
//		}
//		if (request.getParameter("value") != null) {
//			fieldByte = ("value-" + request.getParameter("value")).getBytes();
//			knowledgeRedis.hincrByFloat(keyByte, fieldByte, 1);
//		}
//		if (request.getParameter("curVal") != null) {
//			fieldByte = ("curVal-" + request.getParameter("curVal")).getBytes();
//			knowledgeRedis.hincrByFloat(keyByte, fieldByte, 1);
//		}
//	}
//	
//	/**
//	 * 获取redis中的数据
//	 * @param request
//	 */
//	@RequestMapping(value = "/getKnowledgeFromRedis")
//	@ResponseBody
//	public ModelMap getKnowledgeFromRedis(HttpServletRequest request) {
//		ModelMap model = new ModelMap();
//		ModelMap data = new ModelMap();
//		String key = request.getParameter("key");
////		String field = request.getParameter("field");
////		if(StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
//		if(StringUtils.isEmpty(key)) {
//			buildResponse(model, ResponseEnum.PARAM_ERROR);
//			logger.error("param[key] must not be empty");
//			return model;
//		}
//		byte keyByte[] = key.getBytes();
////		byte fieldByte[] = field.getBytes();
//		Map<byte[], byte[]> result = knowledgeRedis.hgetAll(keyByte);
//		if(result == null) {
//			buildResponse(model, ResponseEnum.SUCCESS, "has no data");
//		} else {
//			for (byte[] keyResult : result.keySet()) {
//				String kResult = new String(keyResult);
//				String vResult = new String(result.get(keyResult));
//				data.put(kResult, vResult);
//			}
//			buildResponse(model, ResponseEnum.SUCCESS, data);
//		}
//		
//		return model;
//	}
//	
//	/**
//	 * 删除redis中的数据
//	 * @param request
//	 */
//	@RequestMapping(value = "/delKnowledgeInRedis")
//	@ResponseBody
//	public ModelMap delKnowledgeInRedis(HttpServletRequest request) {
//		ModelMap model = new ModelMap();
//		ModelMap data = new ModelMap();
//		String key = request.getParameter("key");
//		if(StringUtils.isEmpty(key)) {
//			buildResponse(model, ResponseEnum.PARAM_ERROR);
//			logger.error("param[key] must not be empty");
//			return model;
//		}
//		byte keyByte[] = key.getBytes();
////		byte fieldByte[] = field.getBytes();
//		knowledgeRedis.del(keyByte);
//
//		buildResponse(model, ResponseEnum.SUCCESS);
//		
//		return model;
//	}
//	
//	/*
//	 * 
//	 * 发送邮件测试
//	 */
//	/*
//	@RequestMapping("/sendEmail")
//	@ResponseBody
//	public ModelMap sendEmail(HttpServletRequest request){
//		ModelMap modelMap = new ModelMap();
//		try {
//			String host = "devsmtprelay.paic.com.cn";
//			String user = "pub_dev_mail";
//			String password = "youjianceshi2009@";		
//			String from = "ff-app-sg@pingan.com.cn";
//			String to = "test@pingan.com.cn";
//			String subject = "模块异常";
//			String body = "模块发生异常";
//			Properties props = new Properties();
//			props.put("mail.host", host);
////			props.put("mail.transport.protocol", "smtp");
//			Session session =Session.getInstance(props, null);
//			
//			MimeMessage message = new MimeMessage(session);
//			InternetAddress fromAddress = new InternetAddress(from);
//			message.setFrom(fromAddress);
//			InternetAddress toAddress = new InternetAddress(to);
//			message.addRecipient(Message.RecipientType.TO, toAddress);
//			message.setSubject(subject);
//			message.setContent(body,"text/html;charset=UTF-8");
//			
//			Transport transport = session.getTransport("smtp");
////			transport.connect(host,user,password);
//			transport.connect();
//			transport.sendMessage(message, message.getRecipients((Message.RecipientType.TO)));
//			transport.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return modelMap;
//	}
//	*/
//	
//	@RequestMapping("/test")
//	@ResponseBody
//	public void testCode(HttpServletRequest request) {
//		String key = request.getParameter("key");
//		String field1 = request.getParameter("field1");
//		String field2 = request.getParameter("field2");
//		String field3 = request.getParameter("field3");
////		String value = request.getParameter("value");
//		Map<String,Float> map = new HashMap<String,Float>();
//		map.put(field1, (float) 3.00);
//		map.put(field2, (float) 5);
//		map.put(field3, (float) 6);
//		
//	}
//
//	@RequestMapping("/addToRedis")
//	@ResponseBody
//	public void addToRedis(HttpServletRequest request) {
//		String key = request.getParameter("key");
//	}
//	
//	/**
//	 * 修改redis中的expireTime
//	 * @param request
//	 * @param form
//	 * @return
//	 */
//	@RequestMapping(value = "/updateExpireTime", method = RequestMethod.POST)
//	@ResponseBody
//	public ModelMap updateExpireTime(HttpServletRequest request) {
//		ModelMap model = new ModelMap();
//		try {
//			String time = request.getParameter("time");
//			String key = request.getParameter("key");
//			Long result = knowledgeRedis.hset(key.getBytes(), KnowledgeStrategy.EXPIRETIME_KEYNAME.getBytes(), time.getBytes());
//			if (result != null) {	
//				buildResponse(model, ResponseEnum.SUCCESS);
//			} else {
//				buildResponse(model, ResponseEnum.FAILURE);
//			}
//		} catch (Exception e) {
//			buildResponse(model, ResponseEnum.FAILURE);
//		}
//		return model;
//	}
//	
//	/**
//	 * 用来验证ScanRedis
//	 * @param request
//	 */
//	@RequestMapping(value = "/testScanRedis", method = RequestMethod.GET)
//	@ResponseBody
//	public void testScanRedis(HttpServletRequest request) {
////		Map<String, String> map = new HashMap<String, String>();
////		map.put("appId", "aaa");
////		map.put("os", "aa");
////		String string = JSON.toJSONString(map);
////		System.out.println(string);
//////		JSONObject jsonObject = 
////		Map<String, String> object = JSON.parseObject(string, HashMap.class);
////		System.out.println(object.toString());
//		scanRedis.scanRedis();
//	}
//	
//	/**
//	 * 用来验证SynStrategyBaseInfo
//	 * @param request
//	 */
//	@RequestMapping(value = "/testSynStrategyBaseInfo", method = RequestMethod.GET)
//	@ResponseBody
//	public void testSynStrategyBaseInfo(HttpServletRequest request) {
//		synStrategyBaseInfo.synStrategyBaseInfo();
//	}
//	
//	@RequestMapping(value = "/delKnowledge")
//	@ResponseBody
//	public void delKnowledgeConfig(HttpServletRequest request) {
//		List<KnowledgeStrategyConfigDTO> knowledgeStrategyConfigDTOs = knowledgeStrategyConfigDAO.getKnowledgeStrategyByPage(-1, -1);
//		for (KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO : knowledgeStrategyConfigDTOs) {
//			String opTime = knowledgeStrategyConfigDTO.getOpTime();
//			knowledgeStrategyConfigDAO.deleteKnowledgeStrategyByOpTime(opTime);
//		}
//		List<KnowledgeStrategyStatDTO> knowledgeStrategyStatDTOs = knowledgeStrategyStatDAO.getKnowledgeStrategyStat(-1, -1);
//		for (KnowledgeStrategyStatDTO knowledgeStrategyStatDTO : knowledgeStrategyStatDTOs) {
//			String time = knowledgeStrategyStatDTO.getTime();
//			knowledgeStrategyStatDAO.deleteKnowledgeStrategyStatByOpTime(time);
//		}
//	}
//	
//	@RequestMapping(value = "/dccccc")
//	@ResponseBody
//	public void dccccc(String name, HttpServletRequest request) {
//		KnowledgeStrategyBaseInfoDAO.dropCol(name);
//	}
//}
