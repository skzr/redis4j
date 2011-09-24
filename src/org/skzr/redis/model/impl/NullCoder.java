/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-24 上午10:35:48
 */
package org.skzr.redis.model.impl;

import org.skzr.redis.model.ICoder;

/**
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class NullCoder implements ICoder<Object> {

	@Override
	public byte getKey() {
		throw new IllegalArgumentException("null object coder no key");
	}
	

	@Override
	public byte[] encode(Object value) {
		return null;
	}

	@Override
	public Object decode(byte[] bytes, int off, int len) {
		return null;
	}
}
