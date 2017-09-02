package io.hyperion.govMailNotify.controller;


import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.hyperion.govMailNotify.utils.ResponseUtil;
import io.hyperion.govMailNotify.utils.ResultInfo;

@RequestMapping("/strategy")
@RestController
public class GovMailNotifyController {
    private final Logger logger = Logger.getLogger(getClass());
    
    private final String toMail = "heliuxing902@pingan.com.cn";
    
    @Autowired
	private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;
    
    
    @RequestMapping(value = "/mail")
	@ResponseBody
	public ResultInfo mail(HttpServletRequest request) {
		try {
			String dataAnalysisResult = request.getParameter("dataAnalysisResult");
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("name", "time out");
			return new ResultInfo(ResponseUtil.success_code, "已邮件发送给" + toMail);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
    
    
    @RequestMapping(value = "/govMailNotify")
    public HashMap<String, Object> govMailNotify(@RequestParam String info) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	try {

    		SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom(fromMail);
	        message.setTo(fromMail); //自己给自己发送邮件
	        message.setSubject("主题：简单邮件");
	        message.setText("测试邮件内容");
	        mailSender.send(message);
    		
    		
//    		String to = toMail;	// 收件人
//    		String subject = "服务治理报警邮件";
////    		String text = module + "错误率过大";
//    		String text = info;
////    		SimpleMailMessage message = new SimpleMailMessage();
////    		message.setFrom(fromMail);
////    		message.setTo(to);
////    		message.setSubject(subject);
////    		message.setText(text);
////    		mailSender.send(message);
//    		
//    		SimpleMailMessage message1 = new SimpleMailMessage();
//    		message1.setFrom(fromMail);
//    		message1.setTo(toMail);
//    		message1.setSubject(subject);
//    		message1.setText(text);
//    		mailSender.send(message1);
    		
    		result.put("errno", 0);
    		result.put("message", "SUCCESS");
    		
//    		SslUtils.ignoreSsl();
    		
//	    	Properties props = new Properties();
//	        // 开启debug调试
//	        props.setProperty("mail.debug", "true");  
//	        // 发送服务器需要身份验证  
//	        props.setProperty("mail.smtp.auth", "true");  
//	        // 设置邮件服务器主机名  
//	        props.setProperty("mail.host", "smtp.163.com");  
//	        // 发送邮件协议名称  
//	        props.setProperty("mail.transport.protocol", "smtp");  
//	          
//	        // 设置环境信息  
//	        Session session = Session.getInstance(props);  
//	          
//	        // 创建邮件对象  
//	        Message msg = new MimeMessage(session);  
//	        msg.setSubject("JavaMail测试");  
//	        // 设置邮件内容  
//	        msg.setText("这是一封由JavaMail发送的邮件！");  
//	        // 设置发件人  
//	        msg.setFrom(new InternetAddress("hhh@163.com"));  
//	          
//	        Transport transport = session.getTransport();  
//	        // 连接邮件服务器  
//	        transport.connect("hhh@163.com", "paic1234");
//	        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("hhh@pingan.com.cn", "XX用户", "UTF-8"));
//
//	        // 发送邮件
////	        InternetAddress internetAddress = new InternetAddress("hhh@pingan.com.cn");
////	        Address address = (Address)internetAddress;
//	        transport.sendMessage(msg, msg.getAllRecipients());
//	        // 关闭连接
//	        transport.close();
    		
	        
    	} catch(Exception e) {
    		e.printStackTrace();
    		result.put("errno", -1);
    		result.put("message", "FAIL");
    	}
    	return result;
    }
    
    @RequestMapping(value = "/mailTest")
    public HashMap<String, Object> mailTest() {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	try {
    		result.put("errno", 0);
    		result.put("message", "SUCCESS");
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		result.put("errno", -1);
    		result.put("message", "FAIL");
    	}
    	return result;
    }
}
