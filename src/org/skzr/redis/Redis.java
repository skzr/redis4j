/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-23 下午04:16:37
 */
package org.skzr.redis;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.skzr.redis.exception.RedisIOException;
import org.skzr.redis.model.CoderManager;
import org.skzr.redis.model.RedisReader;
import org.skzr.redis.model.RedisWriter;

/**
 * 代表一个Redis连接<br>
 * 	port default 6379<br>
 * 	timeout default 1000ms (10 seconds)<br>
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class Redis {
	private String hostname;
	private int port = 6379, timeout = 1000;
	private CoderManager coderManager = new CoderManager();
	private Socket socket;
	private RedisWriter writer;
	private RedisReader reader;
	
	/**
	 * 构造一个Redis连接
	 * @param hostname	Redis服务器地址（域名或IP）
	 * @param port	Redis服务端口
	 */
	public Redis(String hostname, Integer port) {
		this.hostname = hostname;
		if (port != null) this.port = port;
	}
	
	public void connect() {
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(hostname, port), timeout);
			socket.setSoTimeout(timeout);
			writer = new RedisWriter(coderManager, socket.getOutputStream());
			reader = new RedisReader(coderManager, socket.getInputStream());
		} catch (IOException e) {
			throw new RedisIOException(e);
		}
	}
	
	public void auth(String password) {
		try {
			writer.send(UtilRedis.firstNoEncoder, Command.AUTH, password);
			writer.flush();
			reader.read();
		} catch (IOException e) {
			throw new RedisIOException(e);
		}
	}
	
	/**
	 * Returns PONG. This command is often used to test if a connection is still alive, or to measure latency.
	 * @return 
	 * @throws IOException 
	 */
	public void ping() {
		try {
			writer.send(Command.PING);
			writer.flush();
			reader.read();
		} catch (IOException e) {
			throw new RedisIOException(e);
		}
	}

	public void select(int index) {
		try {
			writer.send(UtilRedis.firstNoEncoder, Command.SELECT, Integer.toString(index));
			writer.flush();
			reader.read();
		} catch (IOException e) {
			throw new RedisIOException(e);
		}
	}
	
	public void selectAndStore(int index, Object key) {
		select(index);
		set(key, index);
	}
	
	public void set(Object key, Object value) {
		try {
			writer.send(Command.SET, key, value);
			writer.flush();
			reader.read();
		} catch (IOException e) {
			throw new RedisIOException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Object key) {
		try {
			writer.send(Command.GET, key);
			writer.flush();
			return (T) reader.read();
		} catch (IOException e) {
			throw new RedisIOException(e);
		}
	}

	/** {@link #expire(Object, int) call expire(Object, 0)} */
	public boolean expire(Object id) {
		return expire(id, 0);
	}
	/**
	 * 设置过期时间
	 * @param key
	 * @param second
	 * @return
	 */
	public boolean expire(Object key, int second) {
		try {
			writer.send(UtilRedis.secondNoEncoder, Command.EXPIRE, key, String.valueOf(second));
			writer.flush();
			return UtilRedis.one.equals(reader.read());
		} catch (IOException e) {
			throw new RedisIOException(e);
		}
	}
}
