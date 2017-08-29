package me.star2478.jstorm.spout;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import me.star2478.jstorm.JstormMain;
import me.star2478.jstorm.common.PropertiesUtil;
import me.star2478.jstorm.redis.SubscribeListener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class StrategySpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;

	private String redis_strategy_key = PropertiesUtil.getProperty("redis_strategy_key");

	private static Logger logger = Logger.getLogger(StrategySpout.class);

	public void nextTuple() {
		
		try {
			logger.info("StrategySpout start working");
			JedisPool jedisPool = (JedisPool) JstormMain.ctx.getBean("jedisPool");
			Jedis jedis = jedisPool.getResource();
			if (jedis == null) {
				return;
			}
			jedis.subscribe(new SubscribeListener(collector), redis_strategy_key);	//jedis.subscribe会一直堵塞执行，等待消息过来


		} catch (Exception e) {
			
//			e.printStackTrace();
			logger.error("StrategySpout Exception:" + e);
			
		}
		
		//redis发生异常以后休眠5秒重新尝试连接
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("StrategySpout InterruptedException Exception:" + e);
		}

	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(redis_strategy_key));

	}

}
