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
	private static byte[] nullArg = new byte[] {'$', '0', '\r', '\n', '\r', '\n'};
	private CoderManager coderManager = new CoderManager();
	private BufferedOutputStream out;
	
	public RedisWriter(CoderManager coderManager, OutputStream out) {
		this.coderManager = coderManager;
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
	
	private void writeCommand(String value) throws IOException {
		byte[] bytes = value.getBytes(utf8);
		out.write('$');
		writeIntCrLf(bytes.length);
		out.write(bytes);
		out.write(crlf);
	}
	
	public void writeCrLf() throws IOException {
		out.write(crlf);
	}
	public void flush() throws IOException {
		out.flush();
	}
	
	public void send(Command command, Object... arguments) throws IOException {
		send(null, command, arguments);
	}
	
	public void send(boolean[] noEncode, Command command, Object... arguments) throws IOException {
		out.write('*');
		writeIntCrLf(arguments.length + 1);
		
		writeCommand(command.name());
		final boolean noEncodeNotNull = noEncode != null;
		for (int i = 0, len = arguments.length; i < len; i++) {
			Object arg = arguments[i];
			if (arg == null) {
				out.write(nullArg);
			} else {
				ICoder<Object> coder = coderManager.getCoder(arg);
				byte[] raw = coder.encode(arg);
				out.write('$');
				if (noEncodeNotNull && i < noEncode.length && noEncode[i]) {//不编码
					writeIntCrLf(raw.length);
				} else {
					writeIntCrLf(raw.length + 1);
					out.write(coder.getKey());
				}
				out.write(raw);
				out.write(crlf);
			}
		}
	}
}
