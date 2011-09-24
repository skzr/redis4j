/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-24 上午10:35:48
 */
package org.skzr.redis.model.impl;

import java.nio.charset.Charset;

import org.skzr.redis.model.ICoder;

/**
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class StringCoder implements ICoder<String> {
	private static final byte[] empty = new byte[0];
	private static final Charset utf8 = Charset.forName("UTF-8");

	@Override
	public byte getKey() {
		return keyString;
	}

	@Override
	public byte[] encode(String value) {
		return value == null ? null : value.length() == 0 ? empty : value.getBytes(utf8);
	}

	@Override
	public String decode(byte[] bytes, int off, int len) {
		return bytes == null ? null : bytes.length == 0 ? "" : new String(bytes, off, len, utf8);
	}

}
