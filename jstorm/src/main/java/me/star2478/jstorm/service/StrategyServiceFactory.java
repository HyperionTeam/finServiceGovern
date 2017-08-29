package me.star2478.jstorm.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.MidiDevice.Info;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import me.star2478.jstorm.dao.KnowledgeStrategyBaseInfoDAO;
import me.star2478.jstorm.dao.KnowledgeStrategyConfigDAO;
import me.star2478.jstorm.dao.KnowledgeStrategyStatDAO;
import me.star2478.jstorm.redis.KnowledgeRedis;
import me.star2478.jstorm.sqlengine.SQLEngine;


/**
 * 知识策略工厂类，生成指定包装箱对应的策略
 * @author heliuxing
 *
 */
@Service
public class StrategyServiceFactory {

	private static Logger logger = Logger.getLogger(StrategyServiceFactory.class);
	
	@Autowired
	private StrategyServiceString knowledgeStrategyString;
	
	@Autowired
	private StrategyServiceFloat knowledgeStrategyFloat;

	public StrategyServiceFloat getKnowledgeStrategyFloat() {
//		if(knowledgeStrategyFloat == null) {
//			knowledgeStrategyFloat = new KnowledgeStrategyServiceFloat(knowledgeRedis);
//		}
		return knowledgeStrategyFloat;
	}

	public void setKnowledgeStrategyFloat(StrategyServiceFloat knowledgeStrategyFloat) {
		this.knowledgeStrategyFloat = knowledgeStrategyFloat;
	}

	public StrategyServiceString getKnowledgeStrategyString() {
//		if(knowledgeStrategyString == null) {
//			knowledgeStrategyString = new KnowledgeStrategyServiceString(knowledgeRedis);
//		}
		return knowledgeStrategyString;
	}

	public void setKnowledgeStrategyString(StrategyServiceString knowledgeStrategyString) {
		this.knowledgeStrategyString = knowledgeStrategyString;
	}
	
	public StrategyService getKnowledgeStrategyBeanByCommand(String command) {
		if(command == null) {
			return getKnowledgeStrategyString();
		}
		switch (command) {
		case SQLEngine.INC:
			return getKnowledgeStrategyFloat();

		case SQLEngine.SET:
			return getKnowledgeStrategyString();
		}
		return getKnowledgeStrategyString();
	}
	
}
