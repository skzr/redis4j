/**
 * Copyright (c) 2011-2030 by skzr.org
 * All rights reserved.
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @date 2011-9-23 下午04:18:59
 */
package org.skzr.redis;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skzr.redis.exception.RedisAuthException;

/**
 * @author <a href="mailto:skzr.org@gmail.com">skzr.org</a>
 * @version 1.0.0
 * @since JDK1.6
 */
public class RedisTestCase {
	private static String server, password;
	private static Integer port;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server = "127.0.0.1";
		password = "holdcheese.kissme";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Redis redis = new Redis(server, port);
		redis.connect();
		
		Exception ex = null;
		try {
			redis.ping();
		} catch (RedisAuthException e) {
			ex = e;
		}
		Assert.assertTrue(ex instanceof RedisAuthException);
		
		redis.auth(password);
		redis.ping();
		
		redis.select(11);
		redis.selectAndStore(11, "__database");
		Assert.assertEquals(11, redis.get("__database"));
		
		redis.set(123, 123);
		Assert.assertEquals(123, redis.get(123));
		
		Assert.assertTrue(redis.expire(123));
		
		redis.set(123, -123);
		Assert.assertEquals(-123, redis.get(123));
		
		redis.set(0, 65);
		Assert.assertEquals(65, redis.get(0));
		
		redis.set(65, 0);
		Assert.assertEquals(0, redis.get(65));
		
		redis.set(-1, 0);
		Assert.assertEquals(0, redis.get(-1));
		
		redis.set("abc", null);
		Assert.assertEquals(null, redis.get("abc"));
		
		redis.set("", "");
		Assert.assertEquals("", redis.get(""));
		
		Assert.assertEquals(null, redis.get("123"));
		redis.set("", null);
		Assert.assertEquals(null, redis.get(""));
		
		Assert.assertNull(redis.get("哈哈abc"));
	}
	
}
