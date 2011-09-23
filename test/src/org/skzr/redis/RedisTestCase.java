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
	}
}
