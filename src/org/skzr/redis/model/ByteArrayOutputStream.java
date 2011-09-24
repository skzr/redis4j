/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-24 下午09:56:07
 */
package org.skzr.redis.model;

/**
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class ByteArrayOutputStream extends java.io.ByteArrayOutputStream {
	
	public ByteArrayOutputStream(int size) {
		super(size);
	}

	public byte[] getBuf() {
		return buf;
	}
}
