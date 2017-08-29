package me.star2478.jstorm.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
//
//import com.pingan.pafa.redis.Redis;
//import com.pingan.pafa.redis.RedisCommands;
import org.springframework.stereotype.Component;

import clojure.lang.Obj;
import scala.collection.parallel.ParIterableLike.Collect;

/**
 * 
 * @author heliuxing
 *
 */
@Component
public class KnowledgeRedis {

	@Autowired
	private StringRedisTemplate redis;

	public KnowledgeRedis() {
	}

//	public KnowledgeRedis(Redis redis, Class<E> elementClazz) {
//		// this.redis = redis;
//		// this.elementClazz = elementClazz;
//		// this.commands = this.redis.getCommands();
//	}

	public Double hincrByFloat(String key, Object field, Float value) {
		HashOperations<String, Object, Object> ops = redis.opsForHash();//////////////////////是否可以放入static
    	return ops.increment(key, field, value);
	}

	public Long hincrBy(String key, Object field, Long value) {
		return redis.opsForHash().increment(key, field, value);
	}

	public Boolean expire(String key, int expire) {
		return redis.expire(key, expire, TimeUnit.SECONDS);
	}

	public Boolean exists(String key) {
		return redis.hasKey(key);
	}

	public String get(String key) {
		return redis.opsForValue().get(key);
	}

	public String set(String key, String value) {
		redis.opsForValue().set(key, value);
		return value;
	}

	public void hset(String key, String field, Object value) {
		redis.opsForHash().put(key, field, value);
	}

	public void hmset(String key, Map<String, Object> map) {
		redis.opsForHash().putAll(key, map);
	}

	public Long hdel(String key, String... fields) {
		return redis.opsForHash().delete(key, fields);
	}

//	public Long del(byte keyByte[]) {
//		return this.redis.getCommands().del(keyByte);
//	}
//
//	public byte[] hget(byte keyByte[], byte fieldByte[]) {
//		return this.redis.getCommands().hget(keyByte, fieldByte);
//	}

	public Map<String, Object> hgetAll(String key) {
		Map<String, Object> map = new HashMap<String, Object>();
		HashOperations<String, Object, Object> ops = redis.opsForHash();
		Set<Object> fields = ops.keys(key);
		List<Object> list = ops.multiGet(key, fields);
		int i = 0;
		for (Object field : fields) {
			map.put(field.toString(), list.get(i));
			i++;
		}
		return map;
	}
	
	public Map<Object, Object> hgetAllByFields(String key, List<Object> fields) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		List<Object> list = redis.opsForHash().multiGet(key, fields);
		int i = 0;
		for (Object field : fields) {
			map.put(field, list.get(i));
			i++;
		}
		return map;
	}
	
//
//	public Boolean hexists(byte keyByte[], byte fieldByte[]) {
//		return this.redis.getCommands().hexists(keyByte, fieldByte);
//	}

	public List<Object> multiGet(String key, List<Object> fields) {
		return redis.opsForHash().multiGet(key, fields);
	}

	public StringRedisTemplate getRedis() {
		return redis;
	}

	public void setRedis(StringRedisTemplate redis) {
		this.redis = redis;
	}
	

}
