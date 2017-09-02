/**
 * @author yanyueqiang484
 * @desc LogSiteController
 */
//package com.pingan.fLogSearch.dmz.controller;
package io.hyperion.govMailNotify.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;


public class ItsmRequest {
	
//	public static void main(String[] args) throws Exception {
//		handle();
//	}
	
	public static boolean handle() throws Exception {
		String sid = login();
		if (StringUtils.isBlank(sid)) {
			System.out.println("login fail");
			return false;
		}

		String restartData = URLEncoder.encode("{\"ifRestart\":\"restart\",\"restartMethod\":\"serial\",\"restartScore\":\"ALL\",\"ipAddr\":\"\"}", "utf-8");
		String html = URLEncoder.encode("<span></span><table><tr><td id='rh1'>变更说明：</td><td>restart</td></tr></table><span>ff_esbc_consumer_015_stg2环境：</span><table><tr><td id='rh1'>是否重启</td><td>restart</td></tr><tr><td id='rh2'>串行/并行重启:</td><td>串行</td></tr><tr><td id='rh2'>重启范围:</td><td>ALL</td></tr></table>", "utf-8");
		String url = "http://10.11.77.18:9017/ITSM/requestSave?reseaon=restart&user=yanyueqiang484&env=ff_esbc_consumer_015_stg2&configRowsData=[]&sqlRowsData=[]&restartData="+restartData+"&html="+html;
		
		System.out.println(url);
		String ret = httpPostWithCookie(url, null, sid);
		
		if (ret!=null && ret.contains("true")) {
			int left = ret.indexOf("ITSM_");
			int right = ret.indexOf("创建成功");
			if (left!=-1 && right>left) {
				String itsm = ret.substring(left, right);
				
				Thread.sleep(1000);
				String autoUrl = "http://10.11.77.18:9017/ITSM/autodAction?action=startAutod&taskId="+itsm;
				System.out.println(autoUrl);
				
				httpPostWithCookie(autoUrl, null, sid);
			}
		}
		return true;
	}
	
	public static String login() throws Exception {
		HttpPost httppost = new HttpPost("http://10.11.77.18:9017/ITSM/loginAction?userName=yanyueqiang484&password=YANYUEQIANG1988");	
		CloseableHttpClient httpclient = HttpClients.createDefault();
    	
		String sid = "";
        InputStream input = null;
        try {
        	CloseableHttpResponse response = httpclient.execute(httppost);
    		HttpEntity entity = response.getEntity();
    		
    		input = entity.getContent();
        	byte[] ret = IOUtils.toByteArray(input);
        	String retStr = new String(ret, Charset.forName("UTF-8"));
        	
        	if (retStr != null && retStr.contains("true")) {
        		Header[] headers = response.getHeaders("Set-Cookie");
        		for (Header h: headers) {
        			String v = h.getValue();
        			if (v.contains("JSESSIONID")) {
        				int index = v.indexOf(";");
        				if (index < -1) return "";
        				v = v.substring(0, index);
        				System.out.println(v);
        				return v;
        			}
        		}
        	}
        } catch(Exception e) {
        	throw e;
        } finally {
            IOUtils.closeQuietly(input);
            httpclient.close();
        }
        
        return sid;
	}
	
	public static String httpPostWithCookie(String url, HashMap<String, String> params, String cookie) throws Exception {
		HttpPost httppost = new HttpPost(url);
		
		if (params != null && !params.isEmpty()) {
			HttpEntity entry = new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON);
			httppost.setEntity(entry);
		}
		
		return new String(execute(httppost, null, cookie), Charset.forName("UTF-8"));
	}
	
	private static byte[] execute(HttpRequestBase request, OutputStream output, String cookie) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String cookies = cookie;
		request.addHeader("Cookie", cookies);
    	
        InputStream input = null;
        try {
        	CloseableHttpResponse response = httpclient.execute(request);
    		HttpEntity entity = response.getEntity();
    		
    		input = entity.getContent();
        	if ( output == null ) {
        		return IOUtils.toByteArray(input);
        	} else {
        		IOUtils.copy(input, output);
        		output.flush();
        	}
        	
        } catch(Exception e) {
        	throw e;
        } finally {
            IOUtils.closeQuietly(input);
            httpclient.close();
        }

	    return null;
	}
}
