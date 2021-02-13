package com.mdt.util.redistest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis工具类,用于获取RedisPool. 参考官网说明如下： You shouldn't use the same instance from
 * different threads because you'll have strange errors. And sometimes creating
 * lots of Jedis instances is not good enough because it means lots of sockets
 * and connections, which leads to strange errors as well. A single Jedis
 * instance is not threadsafe! To avoid these problems, you should use
 * JedisPool, which is a threadsafe pool of network connections. This way you
 * can overcome those strange errors and achieve great performance. To use it,
 * init a pool: JedisPool pool = new JedisPool(new JedisPoolConfig(),
 * "localhost"); You can store the pool somewhere statically, it is thread-safe.
 * JedisPoolConfig includes a number of helpful Redis-specific connection
 * pooling defaults. For example, Jedis with JedisPoolConfig will close a
 * connection after 300 seconds if it has not been returned.
 * 
 * @author wujintao
 */
public class JedisUtil {
	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 私有构造器.
	 */
	private JedisUtil() {

	}

	private static Map<String, JedisPool> maps = new HashMap<String, JedisPool>();

	/**
	 * 获取连接池.
	 * 
	 * @return 连接池实例
	 */
	public static JedisPool getPool() {
		String ip = "192.168.1.184";
		int port = 6379;
		String key = ip + ":" + port;
		JedisPool pool = null;
		// 这里为了提供大多数情况下线程池Map里面已经有对应ip的线程池直接返回，提高效率
		if (maps.containsKey(key)) {
			pool = maps.get(key);
			return pool;
		}
		// 这里的同步代码块防止多个线程同时产生多个相同的ip线程池
		synchronized (JedisUtil.class) {
			if (!maps.containsKey(key)) {
				JedisPoolConfig config = new JedisPoolConfig();
				// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
				// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
				config.setMaxTotal(500);
				// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
				config.setMaxIdle(5);
				// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
				config.setMaxWaitMillis(1000 * 10);
				config.setTestOnBorrow(true);
				config.setTestOnReturn(true);
				try {
					/**
					 * 如果你遇到 java.net.SocketTimeoutException: Read timed out
					 * exception的异常信息 请尝试在构造JedisPool的时候设置自己的超时值.
					 * JedisPool默认的超时时间是2秒(单位毫秒)
					 */
					pool = new JedisPool(config, ip, port, 10 * 1000);
					maps.put(key, pool);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				pool = maps.get(key);
			}
		}
		return pool;
	}

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
	 */
	private static class RedisUtilHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static JedisUtil instance = new JedisUtil();
	}

	/**
	 * 当getInstance方法第一次被调用的时候，它第一次读取
	 * RedisUtilHolder.instance，导致RedisUtilHolder类得到初始化；而这个类在装载并被初始化的时候，会初始化它的静
	 * 态域，从而创建RedisUtil的实例，由于是静态的域，因此只会在虚拟机装载类的时候初始化一次，并由虚拟机来保证它的线程安全性。
	 * 这个模式的优势在于，getInstance方法并没有被同步，并且只是执行一个域的访问，因此延迟初始化并没有增加任何访问成本。
	 */
	public static JedisUtil getInstance() {
		return RedisUtilHolder.instance;
	}

	/**
	 * 获取Redis实例.
	 * 
	 * @return Redis工具类实例
	 */
	public static Jedis getJedis() {
		Jedis jedis = null;
		int count = 0;
		do {
			try {
				jedis = getPool().getResource();
				// log.info("get redis master1!");
			} catch (Exception e) {
				// log.error("get redis master1 failed!", e);
				// 销毁对象
				getPool().returnBrokenResource(jedis);
			}
			count++;
		} while (jedis == null && count < 10);
		return jedis;
	}

	/**
	 * 释放redis实例到连接池.
	 * 
	 * @param jedis
	 *            redis实例
	 */
	public static void closeJedis(Jedis jedis) {
		if (jedis != null) {
			getPool().returnResource(jedis);
		}
	}

}
