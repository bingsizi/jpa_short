package com.systop.core.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC数据库连接工具类
 * 
 * @author nice
 */
public class JdbcConnFactory {

	//私有构造
	private JdbcConnFactory() {
	}

	private static JdbcConnFactory instance = null;

	/**
	 * 获得实例
	 * @return
	 */
	public static JdbcConnFactory getInstance() {
		if (instance == null) {
			instance = new JdbcConnFactory();
		}
		return instance;
	}

	// 数据库驱动名称
	private String driverName = "net.sourceforge.jtds.jdbc.Driver";
	
	// 数据库连接地址
	private String jdbcUrl = "jdbc:jtds:sqlserver://127.0.0.1:1433/";
	
	// 登陆数据库的用户名。
	private String userName = "sa";
	
	// 登陆数据库的密码。
	private String password = "systop";

	/**
	 * 根据数据库名获得一个新的数据库连接，如果数据库连接创建失败，则返回空的连接对象
	 * 
	 * @param dbName
	 * @return
	 */
	public Connection getConnection(String dbName) {
		Connection conn = null;
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(jdbcUrl + dbName, userName, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 释放关闭资源，可自动适配ResultSet、Statement、Connection，默认情况下仅关闭Connection。
	 * 
	 * @param o
	 */
	public static void close(Object o) {
		if (o == null) {
			return;
		}
		try {
			if (o instanceof ResultSet) {
				((ResultSet) o).close();
				o = null;
			}
			if (o instanceof Statement) {
				((Statement) o).close();
				o = null;
			}
			if (o instanceof Connection) {
				((Connection) o).close();
				o = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 按照先后顺序关闭资源
	 * 
	 * @param rs
	 * @param st
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement st, Connection conn) {
		close(rs);
		close(st);
		close(conn);
	}

	/**
	 * 当不需要ResultSet的情况时，按照顺序先后关闭资源
	 * 
	 * @param st
	 * @param conn
	 */
	public static void close(Statement st, Connection conn) {
		close(st);
		close(conn);
	}

	public static void main(String[] args) {
		Connection conn =JdbcConnFactory.getInstance().getConnection("");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if (conn != null) {
			String sql = "SELECT Name FROM Master..SysDatabases ORDER BY Name ";
			try {
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					System.out.println(rs.getString("name"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(rs, pstmt, conn);
			}
		}
	}
}
