/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-23 下午07:02:39
 */
package org.skzr.redis.model;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.skzr.redis.Command;

/**
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class RedisWriter {
	private static Charset utf8 = Charset.forName("UTF-8");
	private static int intCacheMax = 127;
	private static Map<Integer, byte[]> intCache = new HashMap<Integer, byte[]>(intCacheMax);
	private static byte[] crlf = new byte[] {'\r', '\n'};
	private static byte[] nullArg = new byte[] {'$', '-', '1', '\r', '\n'};
	private BufferedOutputStream out;
	
	public RedisWriter(OutputStream out) {
		this.out = out instanceof BufferedOutputStream ? (BufferedOutputStream) out :
			new BufferedOutputStream(out, 8 * 1024);
	}
	
	private byte[] getIntByte(int value) {
		if (value > intCacheMax) return Integer.toString(value).getBytes(utf8);
		
		byte[] bytes = intCache.get(value);
		if (bytes == null) intCache.put(value, bytes = Integer.toString(value).getBytes(utf8));
		return bytes;
	}
	
	private void writeIntCrLf(int value) throws IOException {
		out.write(getIntByte(value));
		out.write(crlf);
	}
	
	private void sendStrArgument(String value) throws IOException {
		if (value == null) {
			out.write(nullArg);
			return;
		}
		
		byte[] bytes = value.getBytes(utf8);
		out.write('$');
		writeIntCrLf(bytes.length);
		out.write(bytes);
		out.write(crlf);
	}
	
	public void flush() throws IOException {
		out.flush();
	}
	
	public void send(Command command, Object... arguments) throws IOException {
		out.write('*');
		writeIntCrLf(arguments.length + 1);
		sendStrArgument(command.name());
		for (Object argument : arguments) {
			sendStrArgument(argument.toString());
		}
	}
}