package com.trechina.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.trechina.dao.SQLHelper;

@Path("info")
public class EmployeeGet {
	private static Logger logger = Logger.getLogger(EmployeeGet.class);

	@Path("/getEmployee")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONArray Employeeinfo(JSONObject param,
			@Context HttpServletRequest request) {
		String fromSource = "X-Real-IP";
	    String ip = request.getHeader("X-Real-IP");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("X-Forwarded-For");
	      fromSource = "X-Forwarded-For";
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("Proxy-Client-IP");
	      fromSource = "Proxy-Client-IP";
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("WL-Proxy-Client-IP");
	      fromSource = "WL-Proxy-Client-IP";
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getRemoteAddr();
	      fromSource = "request.getRemoteAddr";
	    }
		JSONArray arry = new JSONArray();
		// System.out.println(ip);
		String Employee = null;
		try {
			Employee = param.getString("name");
			//ip = param.getString("flg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.info(
					"Employeeinfo(JSONObject param,@Context HttpServletRequest request)",
					e);
		}
		//String subip = ip.substring(0, ip.lastIndexOf("."));
		ip="192.168.4.193";
		String subip = ip.substring(0, "192.168.4.193".lastIndexOf("."));
		String sql = "SELECT '2'+right('000000000000'+cast(meb.EmployeeCode as varchar),11) as EmployeeCode"
				+ ",meb.familyName as EmployeeName1"
				+ ",meb.FirstName as EmployeeName2"
				+ ",meb.FamilyName_syllabary as EmployeeName_syllabary1"
				+ ",meb.FirstName_syllabary as EmployeeName_syllabary2"
				+ ",(CASE WHEN ma.Store = 0 AND ma.Management1 != 0 "
				+ "THEN (SELECT OrganizationName FROM dbEmployee.dbo.MstOrganization "
				+ "WHERE OrganizationCode = ma.Management1) "
				+ "WHEN ma.Store != 0 AND ma.Management1 != 0 "
				+ "THEN (SELECT OrganizationName FROM dbEmployee.dbo.MstOrganization WHERE OrganizationCode = ma.Store) "
				+ "WHEN ma.Management1 = 0 THEN (SELECT OrganizationName FROM dbEmployee.dbo.MstOrganization "
				+ "WHERE OrganizationCode = ma.Affiliated_company)END) AS StoreName"
				+ ",ISNULL(met.Company_code,ma.Affiliated_company) AS Company_code "
				+ "FROM dbEmployee.dbo.mstEmployeeBasic meb "
				+ "INNER JOIN "
				+ "dbEmployee.dbo.mstAttribute ma "
				+ "ON meb.EmployeeManagementID = ma.EmployeeManagementID "
				+ "LEFT JOIN "
				+ "[dbcyber].[dbo].[MstExceptionTriwel] met "
				+ "ON ma.Store = met.storeCD "
				+ "WHERE meb.EmployeeCode IN ("
				+ Employee + ")";
		// s
		String sqlInsert = "INSERT INTO  [dbcyber].[dbo].[NaFuDaPrintHistory]"
				+ "([IP]"
				+ ",[storeCD]"
				+ ",[EmployeeCode]"
				+ ",[EmployeeName]"
				+ ",[registered])"
				+ "SELECT "
				+ "'"
				+ ip
				+ "'"
				+ ",ISNULL((SELECT [storeCD] FROM [dbCyber].[dbo].[serverIP] WHERE Rtrim(LTRIM(storeIP))= '"
				+ subip + "'),0)" + ",[EmployeeCode]" + ",[EmployeeName]"
				+ ",GETDATE()" + "FROM "
				+ "[dbEmployee].[dbo].[mstEmployeeBasic] " + "WHERE "
				+ "[EmployeeCode] IN (" + Employee + ")";
		/*
		 * "select '2'+right('000000000000'+cast([EmployeeCode] as varchar),11) as EmployeeCode,EmployeeName,EmployeeName_syllabary,"
		 * +
		 * "'飯塚店' as StoreName FROM [MasterDB].[dbo].[Employees] where EmployeeCode in ("
		 * + Employee + ")";
		 */
		Connection conn = SQLHelper.getConnection();
		ResultSet result = SQLHelper.getResultSet(conn, sql);
		if (result != null) {
			try {
				while (result.next()) {
					JSONObject obj = new JSONObject();
					obj.put("EmployeeCode", result.getString(1));
					obj.put("EmployeeName1", result.getString(2));
					obj.put("EmployeeName2", result.getString(3));
					obj.put("EmployeeName_syllabary1", result.getString(4));
					obj.put("EmployeeName_syllabary2", result.getString(5));
					obj.put("StoreName", result.getString(6));
					obj.put("Company_code", result.getString(7));
					/**
					 * obj.put("EmployeeName1", ClassTools.SplitName(0,
					 * result.getString(2))); if
					 * (ClassTools.Namejudge(result.getString(2))) {
					 * obj.put("EmployeeName2", ClassTools.SplitName(1,
					 * result.getString(2))); } else { obj.put("EmployeeName2",
					 * ""); }
					 * 
					 * obj.put("EmployeeName_syllabary1",
					 * ClassTools.SplitName(0, result.getString(3))); if
					 * (ClassTools.Namejudge(result.getString(3))) {
					 * obj.put("EmployeeName_syllabary2",
					 * ClassTools.SplitName(1, result.getString(3))); } else {
					 * obj.put("EmployeeName_syllabary2", ""); }
					 * 
					 * obj.put("StoreName", result.getString(4));
					 ***/
					arry.put(obj);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.info(
						"Employeeinfo(JSONObject param,@Context HttpServletRequest request)",
						e);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				logger.info(
						"Employeeinfo(JSONObject param,@Context HttpServletRequest request)",
						e);
			}
			int fig = SQLHelper.ExecSql(conn, sqlInsert);
		} else {
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.info(
						"Employeeinfo(JSONObject param,@Context HttpServletRequest request)",
						e);
			}
		}
		return arry;
	}

	@Path("/getEmployeeByCD")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONArray getEmployee(JSONObject param) {
		// System.out.println("request:" + param.getString("name"));
		String Employee = null;
		try {
			Employee = param.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.info("getEmployee(JSONObject param)", e);
		}
		//logger.info("getEmployee(JSONObject param)" + Employee);
		JSONArray arry = new JSONArray();
		String sql = "SELECT "
				+ "EmployeeCode "
				+ ",EmployeeName "
				+ ",EmployeeName_syllabary "
				+ ",Employment_title "
				+ ",(CASE WHEN ISNULL(met.Company_code,MA.Affiliated_company) = 10001 "
				+ "THEN 'トライアル' WHEN ISNULL(met.Company_code,MA.Affiliated_company) = 10017 THEN 'トライウェル'"
				+ "ELSE '対応しておりません' END) AS Company_name " + "FROM "
				+ "dbEmployee.dbo.mstEmployeeBasic MEB " + "INNER JOIN "
				+ "dbEmployee.dbo.mstAttribute MA " + "ON "
				+ "MEB.EmployeeManagementID = MA.EmployeeManagementID "
				+ "LEFT JOIN " + "[dbcyber].[dbo].[MstExceptionTriwel] met "
				+ "ON " + "MA.Store = met.storeCD  " + "WHERE EmployeeCode in(" + Employee + ")";
		Connection conn = SQLHelper.getConnection();
		ResultSet result = SQLHelper.getResultSet(conn, sql);
	
		if (result != null) {
			try {
				while (result.next()) {
					// System.out.println("request:" + result.toString());
					JSONObject obj = new JSONObject();
					obj.put("EmployeeCode", result.getString(1));
					obj.put("EmployeeName", result.getString(2));
					obj.put("EmployeeName_syllabary", result.getString(3));
					obj.put("Employment_title", result.getString(4));
					obj.put("Company_name", result.getString(5));
					arry.put(obj);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.info("getEmployee(JSONObject param)", e);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				logger.info("getEmployee(JSONObject param)", e);
			}
		} else {
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.info("getEmployee(JSONObject param)", e);
			}
		}
		return arry;
	}

}
