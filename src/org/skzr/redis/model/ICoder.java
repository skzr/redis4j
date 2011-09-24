/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-24 上午10:26:35
 */
package org.skzr.redis.model;

/**
 * 数据编码器<br>
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public interface ICoder<T> {
	byte keyString = 'a', keyInteger = 'b';
	
	/** 获取编码器标识字节 */
	byte getKey();
	
	/** 编码为字节 */
	byte[] encode(T value);
	
	/** 字节反编码为对象 */
	T decode(byte[] bytes, int off, int len);
}
