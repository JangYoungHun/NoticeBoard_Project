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
import interfaces.ResultSetStrategy;
import interfaces.StatementStratagy;

public class NoticeDao3 {

	JdbcConnection<Notice> jdbcConnection;

	public NoticeDao3(JdbcConnection<Notice> jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}

	public void add(Notice notice) {
		// DB에 notice 추가
		jdbcConnection.update(new StatementStratagy() {
			@Override
			public PreparedStatement createStatement(Connection con) throws SQLException {
				PreparedStatement statement = con
						.prepareStatement("INSERT INTO notices(id,title,author,body,date) " + "VALUES(?,?,?,?,NOW())");
				statement.setInt(1, notice.getId());
				statement.setString(2, notice.getTitle());
				statement.setString(3, notice.getAuthor());
				statement.setString(4, notice.getBody());
				return statement;
			}
		});
	}
 
	public Notice get(int id) {

		ResultSet resultSet = null;
		return jdbcConnection.get(new StatementStratagy() {
			@Override
			public PreparedStatement createStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement("SELECT * FROM notices WHERE id=?");
				statement.setInt(1, id);
				return statement;
			}
		}, new ResultSetStrategy<Notice>() {
			public Notice getResult(ResultSet rs) throws SQLException {
				Notice notice = new Notice();
				notice.setId(rs.getInt("id"));
				notice.setAuthor(rs.getString("author"));
				notice.setDate(rs.getString("date"));
				notice.setBody(rs.getString("body"));
				notice.setTitle(rs.getString("title"));
				return notice;
			};
		});
	}

	// 전체 row count
	public int getCount(){
		ResultSet resultSet = null;
		return jdbcConnection.getCount(new StatementStratagy() {
			@Override
			public PreparedStatement createStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement("SELECT count(*) as count from notices");
				return statement;
			}
		}, new ResultSetStrategy<Integer>() {
			public Integer getResult(ResultSet rs) throws SQLException {
				return rs.getInt("count");
			};
		});

	}

	public List<Notice> getAll(){

		ResultSet resultSet = null;
		return jdbcConnection.getAll(new StatementStratagy() {
			@Override
			public PreparedStatement createStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement("SELECT * FROM notices");
				return statement;
			}
		}, new ResultSetStrategy<Notice>() {
			public Notice getResult(ResultSet rs) throws SQLException {
				Notice notice = new Notice();
				notice.setId(rs.getInt("id"));
				notice.setAuthor(rs.getString("author"));
				notice.setDate(rs.getString("date"));
				notice.setBody(rs.getString("body"));
				notice.setTitle(rs.getString("title"));
				return notice;
			};
		});
	}
	public void deleteAll() {

		jdbcConnection.update(new StatementStratagy() {
			@Override
			public PreparedStatement createStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement("DELETE FROM notices");
				return statement;
			}
		});
	}


}
