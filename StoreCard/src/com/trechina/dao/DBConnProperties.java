package com.trechina.dao;

public class DBConnProperties {
	private String driver;
	private String url;
	private String user;
	private String pwd;

	public DBConnProperties() {

	}

	public DBConnProperties(String driver, String url, String user, String pwd) {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}