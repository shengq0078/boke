package com.trechina.dao;

import java.sql.*;

import org.apache.log4j.Logger;

/**
 * SQL JDBCで データベースを簡単操作する このクラスに実例化禁止
 * 
 * @author 盛強
 */
public class SQLHelper {
	/**
	 * ドライバ
	 */
	public static String driver;
	/**
	 * アドレス
	 */
	public static String url;
	/**
	 * ＩＤ
	 */
	public static String user;
	/**
	 * パスワード
	 */
	public static String password;
	private static Logger logger = Logger.getLogger(SQLHelper.class);
	static {
		DBConnProperties dbcp = new DBConnProperties();
		dbcp = AnalyzeDBConn.getDBConnProperties();
		driver = dbcp.getDriver();
		url = dbcp.getUrl();
		user = dbcp.getUser();
		password = dbcp.getPwd();
	}

	private SQLHelper() {
	}

	/**
	 * 获取一个数据库连接 通过设置类的 driver / url / user / password 这四个静态变量来 设置数据库连接属性
	 * 
	 * @return 数据库连接
	 */
	public static Connection getConnection() {
		try {
			// 获取驱动,这里使用的是 sqljdbc_1.2.2828.100_chs.exe,不同版本的驱动,语句有所不同
			Class.forName(driver);
		} catch (ClassNotFoundException ex) {
			logger.info("getConnection", ex);
		}
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException ex) {
			logger.info("getConnection", ex);
			return null;
		}
	}

	/**
	 * 获取一个 Statement 该 Statement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static Statement getStatement() {
		Connection conn = getConnection();
		if (conn == null) {
			return null;
		}
		try {
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			// 设置数据集可以滚动,可以更新
		} catch (SQLException ex) {
			logger.info("getStatement", ex);
			close(conn);
		}
		return null;
	}

	/**
	 * 获取一个 Statement 该 Statement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @param conn
	 *            数据库连接
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static Statement getStatement(Connection conn) {
		if (conn == null) {
			return null;
		}
		try {
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			// 设置数据集可以滚动,可以更新
		} catch (SQLException ex) {
			logger.info("getStatement(Connection conn)", ex);
			return null;
		}
	}

	/**
	 * 获取一个带参数的 PreparedStatement 该 PreparedStatement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static PreparedStatement getPreparedStatement(String cmdText,
			Object... cmdParams) {
		Connection conn = getConnection();
		if (conn == null) {
			return null;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn
					.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			int i = 1;
			for (Object item : cmdParams) {
				pstmt.setObject(i, item);
				i++;
			}
		} catch (SQLException e) {
			logger.info(
					"getPreparedStatement(String cmdText,Object... cmdParams)",
					e);
			close(conn);
		}
		return pstmt;
	}

	/**
	 * 获取一个带参数的 PreparedStatement 该 PreparedStatement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static PreparedStatement getPreparedStatement(Connection conn,
			String cmdText, Object... cmdParams) {
		if (conn == null) {
			return null;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(cmdText);
			int i = 1;
			for (Object item : cmdParams) {
				pstmt.setString(i, item.toString());
				i++;
			}
		} catch (SQLException e) {
			logger.info(
					"getPreparedStatement(Connection conn,String cmdText,Object... cmdParams)",
					e);
			close(pstmt);
		}
		return pstmt;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int ExecSql(String cmdText) {
		Statement stmt = getStatement();
		if (stmt == null) {
			return -2;
		}
		int i;
		try {
			i = stmt.executeUpdate(cmdText);
		} catch (SQLException ex) {
			logger.info(" ExecSql(String cmdText)", ex);
			i = -1;
		}
		closeConnection(stmt);
		return i;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int ExecSql(Connection conn, String cmdText) {
		Statement stmt = getStatement(conn);
		if (stmt == null) {
			return -2;
		}
		int i;
		try {
			i = stmt.executeUpdate(cmdText);
		} catch (SQLException ex) {
			logger.info("ExecSql(Connection conn, String cmdText)", ex);
			i = -1;
		}
		close(stmt);
		return i;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int ExecSql(String cmdText, Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
		if (pstmt == null) {
			return -2;
		}
		int i;
		try {
			i = pstmt.executeUpdate();
		} catch (SQLException ex) {
			logger.info("ExecSql(String cmdText, Object... cmdParams)", ex);
			i = -1;
		}
		closeConnection(pstmt);
		return i;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int ExecSql(Connection conn, String cmdText,
			Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
		if (pstmt == null) {
			return -2;
		}
		int i;
		try {
			i = pstmt.executeUpdate();
		} catch (SQLException ex) {
			logger.info(
					"ExecSql(Connection conn, String cmdText,Object... cmdParams)",
					ex);
			i = -1;
		}
		close(pstmt);
		return i;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static Object ExecScalar(String cmdText) {
		ResultSet rs = getResultSet(cmdText);
		Object obj = buildScalar(rs);
		closeConnection(rs);
		return obj;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static Object ExecScalar(Connection conn, String cmdText) {
		ResultSet rs = getResultSet(conn, cmdText);
		Object obj = buildScalar(rs);
		closeEx(rs);
		return obj;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static Object ExecScalar(String cmdText, Object... cmdParams) {
		ResultSet rs = getResultSet(cmdText, cmdParams);
		Object obj = buildScalar(rs);
		closeConnection(rs);
		return obj;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static Object ExecScalar(Connection conn, String cmdText,
			Object... cmdParams) {
		ResultSet rs = getResultSet(conn, cmdText, cmdParams);
		Object obj = buildScalar(rs);
		closeEx(rs);
		return obj;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static ResultSet getResultSet(String cmdText) {
		Statement stmt = getStatement();
		if (stmt == null) {
			return null;
		}
		try {
			return stmt.executeQuery(cmdText);
		} catch (SQLException ex) {
			logger.info("getResultSet(String cmdText))", ex);
			closeConnection(stmt);
		}
		return null;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param conn
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static ResultSet getResultSet(Connection conn, String cmdText) {
		Statement stmt = getStatement(conn);
		if (stmt == null) {
			return null;
		}
		try {
			return stmt.executeQuery(cmdText);
		} catch (SQLException ex) {
			logger.info("getResultSet(Connection conn, String cmdText)", ex);
			close(stmt);
		}
		return null;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static ResultSet getResultSet(String cmdText, Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
		if (pstmt == null) {
			return null;
		}
		try {
			return pstmt.executeQuery();
		} catch (SQLException ex) {
			logger.info("getResultSet(String cmdText, Object... cmdParams)", ex);
			closeConnection(pstmt);
		}
		return null;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static ResultSet getResultSet(Connection conn, String cmdText,
			Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
		if (pstmt == null) {
			return null;
		}
		try {
			return pstmt.executeQuery();
		} catch (SQLException ex) {
			logger.info(
					"getResultSet(Connection conn, String cmdText,Object... cmdParams)",
					ex);
			close(pstmt);
		}
		return null;
	}

	public static Object buildScalar(ResultSet rs) {
		if (rs == null) {
			return null;
		}
		Object obj = null;
		try {
			if (rs.next()) {
				obj = rs.getObject(1);
			}
		} catch (SQLException ex) {
			logger.info("buildScalar", ex);
		}
		return obj;
	}

	/**
	 * 获取一个具有更新功能的数据模型 如果只要读取数据，就不要用它了
	 * 
	 * @param cmdText
	 *            能返回一个数据集的查询SQL 语句
	 * @return 表格数据模型
	 * 
	 * 
	 *         DataSet 没有找到在哪个包中,因为暂时用不到所以省略此方法
	 * 
	 *         public static DataSet getDataSet(String cmdText) { Statement stmt
	 *         = getStatement(); DataSet dbc = new DataSet(); if (stmt == null)
	 *         { dbc.code = -2; return dbc; } try { // 查询语句 dbc.rs =
	 *         stmt.executeQuery(cmdText); dbc.model = buildTableModel(dbc.rs);
	 *         dbc.code = dbc.model.getRowCount(); } catch (SQLException ex) {
	 *         Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
	 *         null, ex); dbc.code = -1; } return dbc; }
	 */

	/**
	 * 获取一个具有更新功能的数据模型 如果只要读取数据，就不要用它了
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            能返回一个数据集的查询SQL 语句
	 * @return 表格数据模型
	 * 
	 *         同上一个方法
	 * 
	 *         public static DataSet getDataSet(Connection conn, String cmdText)
	 *         { Statement stmt = getStatement(conn); DataSet dbc = new
	 *         DataSet(); if (stmt == null) { dbc.code = -2; return dbc; } try {
	 *         // 查询语句 dbc.rs = stmt.executeQuery(cmdText); dbc.model =
	 *         buildTableModel(dbc.rs); dbc.code = dbc.model.getRowCount(); }
	 *         catch (SQLException ex) {
	 *         Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
	 *         null, ex); dbc.code = -1; } return dbc; }
	 */
	/**
	 * 获取一个具有更新功能的数据模型 如果只要读取数据，就不要用它了
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 表格数据模型
	 * 
	 * 
	 *         同上一个方法 *
	 * 
	 * 
	 *         public static DataSet getDataSet(String cmdText, Object...
	 *         cmdParams) { PreparedStatement pstmt =
	 *         getPreparedStatement(cmdText, cmdParams); DataSet dbc = new
	 *         DataSet(); if (pstmt == null) { dbc.code = -2; return dbc; } try
	 *         { // 查询语句 dbc.rs = pstmt.executeQuery(); dbc.model =
	 *         buildTableModel(dbc.rs); dbc.code = dbc.model.getRowCount(); }
	 *         catch (SQLException ex) {
	 *         Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
	 *         null, ex); dbc.code = -1; } return dbc; }
	 */
	/**
	 * 获取一个具有更新功能的数据模型 如果只要读取数据，就不要用它了
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 表格数据模型
	 * 
	 * 
	 *         同上
	 * 
	 * 
	 *         public static DataSet getDataSet(Connection conn, String cmdText,
	 *         Object... cmdParams) { PreparedStatement pstmt =
	 *         getPreparedStatement(conn, cmdText, cmdParams); DataSet dbc = new
	 *         DataSet(); if (pstmt == null) { dbc.code = -2; return dbc; } try
	 *         { // 查询语句 dbc.rs = pstmt.executeQuery(); dbc.model =
	 *         buildTableModel(dbc.rs); dbc.code = dbc.model.getRowCount(); }
	 *         catch (SQLException ex) {
	 *         Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
	 *         null, ex); dbc.code = -1; } return dbc; }
	 */
	private static void close(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			logger.info("close(Object obj)", ex);
		}
	}

	private static void closeEx(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).getStatement().close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			logger.info("closeEx(Object obj)", ex);
		}
	}

	private static void closeConnection(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).getConnection().close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).getConnection().close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).getStatement().getConnection().close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			logger.info("closeConnection(Object obj)", ex);
		}
	}
}
