/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-23 下午05:32:43
 */
package org.skzr.redis.exception;

import java.io.IOException;

/**
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class RedisException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public RedisException(IOException e) {
		super(e);
	}
	public RedisException(String message) {
		super(message);
	}
}
