package JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcClose {
	
	private static JdbcClose instance;
	
	private JdbcClose() {
		
	}
	
	public static JdbcClose getInstance() {
		if(instance == null)
			instance = new JdbcClose();
		return instance;
	}
	
	
	public static void close(Connection con) {
		if(con != null) {
			try {
				con.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public static void close(Statement statement) {
		if(statement != null) {
			try {
				statement.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	

	public static void close(ResultSet resultSet) {
		if(resultSet != null) {
			try {
				resultSet.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
