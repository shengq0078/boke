package com.trechina.dao;

import java.util.Properties;

public class AnalyzeDBConn {
	public static DBConnProperties getDBConnProperties() {

		// 用于连接并且返回的对象
		DBConnProperties dbcp = new DBConnProperties();
		Properties pros = OAuthConfig.getPros();
		dbcp.setDriver(pros.getProperty("driver"));
		dbcp.setUrl(pros.getProperty("url"));
		dbcp.setUser(pros.getProperty("user"));
		dbcp.setPwd(pros.getProperty("pwd"));
		return dbcp;
	}

	public static DBConnProperties getDBConnProperties1() {

		// 用于连接并且返回的对象
		DBConnProperties dbcp = new DBConnProperties();
		Properties pros = OAuthConfig.getPros();
		dbcp.setDriver(pros.getProperty("driver1"));
		dbcp.setUrl(pros.getProperty("url1"));
		dbcp.setUser(pros.getProperty("user1"));
		dbcp.setPwd(pros.getProperty("pwd1"));
		return dbcp;
	}
}