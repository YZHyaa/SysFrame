package com.mdt.util.redis;

import com.mdt.util.redistest.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MsgQueue {

	/**
	 * 在不同的线程中使用相同的Jedis实例会发生奇怪的错误。但是创建太多的实现也不好因为这意味着会建立很多sokcet连接，
	 * 也会导致奇怪的错误发生。单一Jedis实例不是线程安全的。为了避免这些问题，可以使用JedisPool,
	 * JedisPool是一个线程安全的网络连接池。可以用JedisPool创建一些可靠Jedis实例，可以从池中拿到Jedis的实例。
	 * 这种方式可以解决那些问题并且会实现高效的性能
	 */

	public static void main(String[] args) {
		MsgQueue testCase = new MsgQueue();
		testCase.pushMsg("1", "error", "报警消息");
		// ...when closing your application:
		// JedisUtil.getPool().destroy();
	}

	/**
	 * 获取用户未读消息，每次调用返回一条 <br>
	 * redis-server TestCase: <br>
	 * redis-cli <br>
	 * lpush msg:hasnotshow:1 error###报警消息###1<br>
	 * lpush msg:hasnotshow:1 error###报警消息2###2
	 * 
	 * @param uId
	 *            用户ID
	 * @param crtNo
	 *            消息序号
	 * @return 消息内容
	 */
	public String getNewMsg(String uId, String crtNo) {
		Jedis jedis = JedisUtil.getJedis();
		if (jedis != null) {
			// jedis.flushAll();
			// List<String> hasnotshow = jedis.lrange("msg:hasshow:"+uId, 0,
			// -1);
			// System.out.println(hasnotshow);
			// final MyListener listener = new MyListener();
			// jedis.subscribe(listener, "msg");
			List<String> hasnotshow = jedis.lrange("msg:hasnotshow:" + uId, 0,
					-1);
			hasnotshow = convrtOrder(hasnotshow, crtNo);
			for (int i = 0; i < hasnotshow.size(); i++) {
				if (hasnotshow.get(i) != null
						&& hasnotshow.get(i).indexOf("###") != -1) {
					int msgNo = Integer
							.parseInt(hasnotshow.get(i).split("###")[2]);
					if (crtNo != null && msgNo > Integer.parseInt(crtNo)) {
						return hasnotshow.get(i);
					}
				}
			}
			System.out.println(jedis.brpop(0, "msg:hasnewmsg:" + uId)
					.toString());
			jedis.close();
			return "";
		} else {
			return "syserror###消息提醒组件异常！###0";
		}

	}

	private List<String> convrtOrder(List<String> list, String crtNo) {
		Collections.sort(list, new Comparator<String>() {
			public int compare(String arg0, String arg1) {
				System.out.println(arg0);
				if (arg0.split("###").length == 3
						&& arg1.split("###").length == 3) {
					Integer ord1 = Integer.parseInt(arg0.split("###")[2]);
					Integer ord2 = Integer.parseInt(arg1.split("###")[2]);
					return ord1.compareTo(ord2);
				}
				return -1;

			}
		});
		return list;
	}

	/**
	 * 向redis推送消息
	 * 
	 * @param uId
	 *            推送用户ID
	 * @param msgType
	 *            消息类型：success info warning error
	 * @param msgContent
	 *            消息内容
	 * @return
	 */
	public Boolean pushMsg(String uId, String msgType, String msgContent) {
		try {
			Jedis jedis = JedisUtil.getJedis();
			String msg = msgType + "###" + msgContent + "###"
					+ jedis.incr("msg.id");
			// 向客户端推送新消息
			jedis.lpush("msg:hasnotshow:" + uId, msg);
			// 新消息提醒
			for (int i = 0; i < 100; i++) {
				jedis.lpush("msg:hasnewmsg:" + uId, msg);
			}
			jedis.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
