package JDBC3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import JDBC.JdbcClose;
import dataclass.Notice;
import dataclass.User;
import interfaces.ResultSetStrategy;
import interfaces.StatementStratagy;

public class UserDao3 {

		JdbcConnection<User> jdbcConnection;

		public UserDao3(JdbcConnection<User> jdbcConnection) {
			this.jdbcConnection = jdbcConnection;
		}
		
		public void add(String id, String pwd) throws RuntimeException{
			// DB에 notice 추가
			jdbcConnection.update(new StatementStratagy() {
				@Override
				public PreparedStatement createStatement(Connection con) throws SQLException {
					PreparedStatement statement = con
							.prepareStatement("INSERT INTO user(id,pwd,created) "+"VALUES(?,?,NOW())");
					statement.setString(1, id);
					statement.setString(2, pwd);
					return statement;
				}
			});
		}
		public User get(String id) throws RuntimeException {
			return jdbcConnection.get(new StatementStratagy() {
				@Override
				public PreparedStatement createStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement("SELECT * FROM user WHERE id=?");
					statement.setString(1, id);
					return statement;
				}
			}, new ResultSetStrategy<User>() {
				public User getResult(ResultSet rs) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setPwd(rs.getString("pwd"));
					user.setCreated(rs.getString("created"));
					return user;
				};
			});

		}
		// 전체 row count
		public int getCount() throws RuntimeException {
			ResultSet resultSet = null;
			return jdbcConnection.getCount(new StatementStratagy() {
				@Override
				public PreparedStatement createStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement("SELECT count(*) as count from user");
					return statement;
				}
			}, new ResultSetStrategy<Integer>() {
				public Integer getResult(ResultSet rs) throws SQLException {
					return rs.getInt("count");
				};
			});

		}

		public List<User> getAll() throws RuntimeException {	
 
			ResultSet resultSet = null;
			return jdbcConnection.getAll(new StatementStratagy() {
				@Override
				public PreparedStatement createStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement("SELECT * FROM user");
					return statement;
				}
			}, new ResultSetStrategy<User>() {
				public User getResult(ResultSet rs) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setPwd(rs.getString("pwd"));
					user.setCreated(rs.getString("created"));
					return user;
				};
			});

		}

		public void deleteAll() throws RuntimeException {

			jdbcConnection.update(new StatementStratagy() {
				@Override
				public PreparedStatement createStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement("DELETE FROM user");
					return statement;
				}
			});
		}



	
}
