/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-23 下午07:02:39
 */
package org.skzr.redis.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.skzr.redis.exception.RedisAuthException;
import org.skzr.redis.exception.RedisIOException;
import org.skzr.redis.exception.RedisOpException;

/**
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class RedisReader {
	private static String utf8S = "UTF-8";
	private boolean ended = false;
	private InputStream in;
	private byte[] buf = new byte[8 * 1024];
	private int pos = 0, len = 0;

	public RedisReader(InputStream in) {
		this.in = in;
	}
	
	/** 读取缓存 */
	private void fill() throws IOException {
		if (ended) throw new RedisIOException("the end of the stream has been reached");
		
		if (len == 0) {
			pos = 0;
			len = in.read(buf);
			if (ended = (len == -1)) throw new RedisIOException("the end of the stream has been reached");
		}
	}
	/** 寻找CrLf(-1未找到, -2 cr结尾, 其他找到) */
	private static int findCrLf(byte[] b, int off, int len) {
		for (int i = off, max = off + len; i < max; i++) {
			if (b[i] == '\r') {
				if (i == max - 1) return -2;
				if (b[i + 1] == '\n') return i;
			}
		}
		return -1;
	}
	
	private ByteArrayOutputStream readCrLf() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(2 * (len > 128 ? len : 128));
		for (int lastFind = -1, find = -1;; lastFind = find) {
			fill();
			if (lastFind == -2) {
				if (buf[0] == '\n') {
					pos++;
					len--;
					break;
				}
				bout.write('\r');
			}
			
			find = findCrLf(buf, pos, len);
			if (find >= 0) {//找到
				final int off = pos, length = find - pos;
				pos = find + 2;
				len -= length + 2;
				if (length > 0) bout.write(buf, off, length);
				break;
			} else if (find == -1) {
				bout.write(buf, pos, len);
				len = 0;
			}
		}
		
		return bout;
	}
	
	private String readStatusReply() throws IOException {
		ByteArrayOutputStream bout = readCrLf();
		return bout.toString(utf8S);
	}
	
	private void readErrorReply() throws IOException, RedisOpException {
		ByteArrayOutputStream bout = readCrLf();
		String message = bout.toString(utf8S);
		throw "ERR operation not permitted".equals(message) || "ERR invalid password".equals(message) ?
				new RedisAuthException(message) : new RedisOpException(message);
	}

	public Object read() throws IOException {
		fill();
		char first = (char) buf[pos++];
		len--;
		switch (first) {
		case '+':
			return readStatusReply();
		case '-':
			readErrorReply();
		default:
			break;
		}
		return null;
	}
	
}
