package com.trechina.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.mail.handlers.text_html;

import jxl.common.LengthConverter;

public class Test {
	public static void main(String[] args) throws SQLException {// 可以通过编译，而且能运行
		/*System.out.println("test ok!");
		System.out.println("test ok1!");
		System.out.println("test ok2!");
		System.out.println("test ok3!");
		String aaaaaa = "10083619 as EmployeeName";
		String aaString = "10083619";
		String aa = null;
		Connection conn = SQLHelper.getConnection();
		//String sql = "select EmployeeCode,EmployeeName,EmployeeName_syllabary,"
			//	+ "'飯塚店' as StoreName FROM [MasterDB].[dbo].[Employees] where EmployeeCode in (?,?,?,?)";
		//ResultSet rs = SQLHelper.getResultSet(conn,sql, "10083619","10083620","10083621","10083622");
		String sql = "select EmployeeCode,?,EmployeeName_syllabary,"
				+ "'飯塚店' as StoreName FROM [MasterDB].[dbo].[Employees] where EmployeeCode in (?)";
		ResultSet rs = SQLHelper.getResultSet(conn,sql,aaaaaa);
	
		try {
			while (rs.next()) {
				aa = rs.getString("EmployeeName").toString();
				System.out.println("test ok4!" + aa);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (conn!=null) {
			conn.close();
		}*/
		String sub="192.168.4.176".substring(0,"192.168.4.176".lastIndexOf("."));
		String aa=Test.SplitName(0,"大場恵未");
		System.out.println("test ok4!" + aa);
	}
	public static String SplitName(int i,String strName) {
		String nameSp=null;
		String[] name=strName.split(" ");
		if(name.length>1){
			nameSp=name[i]; 
		}
		return nameSp;
	}
}
