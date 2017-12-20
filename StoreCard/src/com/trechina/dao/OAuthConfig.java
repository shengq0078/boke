package com.trechina.dao;

import java.io.IOException;
import java.util.Properties;

public class OAuthConfig {
	private static Properties pros = new Properties();
	static {
		try {
			// 加载config.properties
			pros.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Properties getPros() {
		return pros;
	}
}