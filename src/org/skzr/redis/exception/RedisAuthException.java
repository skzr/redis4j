/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-23 下午05:32:43
 */
package org.skzr.redis.exception;


/**
 * 授权异常
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class RedisAuthException extends RedisOpException {
	private static final long serialVersionUID = 1L;
	
	public RedisAuthException(String message) {
		super(message);
	}
}
