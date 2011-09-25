/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-24 上午10:32:12
 */
package org.skzr.redis.model;

import java.util.HashMap;
import java.util.Map;

import org.skzr.redis.model.impl.IntegerCoder;
import org.skzr.redis.model.impl.StringCoder;

/**
 * 编码管理器
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class CoderManager {
	private Map<Class<?>, ICoder<?>> defaultCoders = new HashMap<Class<?>, ICoder<?>>();
	private Map<Byte, ICoder<?>> defaultCodersByKey = new HashMap<Byte, ICoder<?>>();
	private Map<Class<?>, ICoder<?>> coders = new HashMap<Class<?>, ICoder<?>>();
	private Map<Byte, ICoder<?>> codersByKey = new HashMap<Byte, ICoder<?>>();
	
	{
		defaultCoders.put(String.class, new StringCoder());
		defaultCoders.put(Integer.class, new IntegerCoder());
		
		for (ICoder<?> coder : defaultCoders.values()) {
			defaultCodersByKey.put(coder.getKey(), coder);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> ICoder<T> getDefaultCoder(T object) {
		return (ICoder<T>) defaultCoders.get(object == null ? null : object.getClass());
	}
	
	@SuppressWarnings("unchecked")
	private <T> ICoder<T> getDefaultCoderByKey(byte key) {
		return (ICoder<T>) defaultCodersByKey.get(key);
	}

	public <T> ICoder<T> getCoder(T object) {
		@SuppressWarnings("unchecked")
		ICoder<T> coder = (ICoder<T>) coders.get(object == null ? null : object.getClass());
		return coder == null ? getDefaultCoder(object) : coder;
	}

	public <T> ICoder<T> getCoderByKey(byte key) {
		@SuppressWarnings("unchecked")
		ICoder<T> coder = (ICoder<T>) codersByKey.get(key);
		if (coder != null) return coder;
		return getDefaultCoderByKey(key);
	}

}
