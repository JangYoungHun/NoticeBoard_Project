package JDBC3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

import JDBC.JdbcClose;
import interfaces.ResultSetStrategy;
import interfaces.StatementStratagy;

public class JdbcConnection<T> {

	DataSource dataSource;

	public JdbcConnection(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void update(StatementStratagy callback) {
		Connection con = null;
		PreparedStatement stm = null;

		try {
			con = dataSource.getConnection();
			stm = callback.createStatement(con);
			stm.executeUpdate();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JdbcClose.close(con);
			JdbcClose.close(stm);
		}
	}

	public T get(StatementStratagy callback, ResultSetStrategy<T> result) {
		T object = null;
		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			stm = callback.createStatement(con);
			rs = stm.executeQuery();
			rs.next();
			object = result.getResult(rs);
			return object;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JdbcClose.close(con);
			JdbcClose.close(stm);
			JdbcClose.close(rs);
		}
	}
	
	public int getCount(StatementStratagy callback, ResultSetStrategy<Integer> result) {
		int count;
		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			stm = callback.createStatement(con);
			rs = stm.executeQuery();
			rs.next();
			count = result.getResult(rs);
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JdbcClose.close(con);
			JdbcClose.close(stm);
			JdbcClose.close(rs);
		}
	}
	

	public List<T> getAll(StatementStratagy callback, ResultSetStrategy<T> result) {
		List<T> objects = new ArrayList<T>();
		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			stm = callback.createStatement(con);
			rs = stm.executeQuery();
			while (rs.next()) {
				objects.add(result.getResult(rs));
			}
			return objects;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JdbcClose.close(con);
			JdbcClose.close(stm);
			JdbcClose.close(rs);
		}
	}

}
