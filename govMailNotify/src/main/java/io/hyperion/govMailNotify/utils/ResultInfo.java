package io.hyperion.govMailNotify.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ResultInfo{
	private static SerializerFeature[] features = 
		{ SerializerFeature.WriteMapNullValue // 输出空置字段
		, SerializerFeature.WriteNullListAsEmpty // list字段如果为null，输出为[]，而不是null
		, SerializerFeature.WriteNullNumberAsZero // 数值字段如果为null，输出为0，而不是null
		, SerializerFeature.WriteNullBooleanAsFalse // Boolean字段如果为null，输出为false，而不是null
		, SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null		
		};
	
	private String code;
	private String msg;
	private Object data;
 

	
	public ResultInfo(String code) {
		this.code = code;
		this.msg = ResponseUtil.getMsgByCode(code);
		this.data = "";
	}

	public ResultInfo(String code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ResultInfo(String code, Object data) {
		this.code = code;
		this.msg = ResponseUtil.getMsgByCode(code);
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getmsg() {
		return msg;
	}

	public void setmsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ResultInfo [code=" + code + ", msg=" + msg + "]";
	}
	
	public String toJSONString() {		
		String str = JSONObject.toJSONString(this, features);
		return str;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}

