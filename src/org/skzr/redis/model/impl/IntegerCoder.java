/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-24 下午09:25:12
 */
package org.skzr.redis.model.impl;

import org.skzr.redis.model.ICoder;

/**
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class IntegerCoder implements ICoder<Integer>{
	
	public static Integer toInteger(byte[] bytes) {
		return toInteger(bytes, 0, bytes.length);
	}
	public static Integer toInteger(byte[] bytes, int off, int len) {
		if (bytes == null || bytes.length == 0) return null;
		return bytes[3 + off] << 24 | (bytes[2 + off] & 0xff) << 16 | (bytes[1 + off] & 0xff) << 8 | (bytes[0 + off] & 0xff);
	}

	@Override
	public byte getKey() {
		return keyInteger;
	}

	@Override
	public byte[] encode(Integer value) {
		if (value == null) return null;
		int v = value.intValue();
		return new byte[] {(byte) v, (byte) (v >> 8), (byte) (v >> 16), (byte) (v >>> 24)};
	}

	@Override
	public Integer decode(byte[] bytes, int off, int len) {
		return toInteger(bytes, off, len);
	}

}
