package io.hyperion.govMailNotify.controller;


import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


@RestController
public class GovMailNotifyController {
    private final Logger logger = Logger.getLogger(getClass());
    
    private final String fromMail = "hhh@163.com";
    private final String toMail = "hhh@163.com";
    
    @Autowired
	private JavaMailSender mailSender;
    
    @RequestMapping(value = "/govMailNotify")
    public HashMap<String, Object> govMailNotify(@RequestParam String info) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	try {
//    		JSONObject map = JSON.parseObject(info);
//    		String module = (String) map.get("module");
    		String to = toMail;	// 收件人
    		String subject = "服务治理报警邮件";
//    		String text = module + "错误率过大";
    		String text = info;
//    		SimpleMailMessage message = new SimpleMailMessage();
//    		message.setFrom(fromMail);
//    		message.setTo(to);
//    		message.setSubject(subject);
//    		message.setText(text);
//    		mailSender.send(message);
    		
    		SimpleMailMessage message1 = new SimpleMailMessage();
    		message1.setFrom(fromMail);
    		message1.setTo(toMail);
    		message1.setSubject(subject);
    		message1.setText(text);
    		mailSender.send(message1);
    		
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
