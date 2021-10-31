package JDBC4;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import dataclass.User;

public class UserDao4 {

	JdbcTemplate jdbcTemplate;

		public UserDao4(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
		}
		
		public void add(String id, String pwd) throws RuntimeException{
			// DB에 notice 추가
			jdbcTemplate.update("INSERT INTO user(id,pwd,created) "+"VALUES(?,?,NOW())",id,pwd);
		}
		
		public User get(String id) throws RuntimeException {
			return jdbcTemplate.queryForObject("SELECT * FROM user WHERE id=?", new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setPwd(rs.getString("pwd"));
					user.setCreated(rs.getString("created"));
					return user;
				}
			}, id);				
		}
		
		// 전체 row count
		public int getCount() throws RuntimeException {
			
			return jdbcTemplate.queryForObject("SELECT count(*) as count from user", new RowMapper<Integer>() {
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getInt("count");
				}
			});
		}

		public List<User> getAll() throws RuntimeException {	
			
			return jdbcTemplate.query("SELECT * FROM user", new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setPwd(rs.getString("pwd"));
					user.setCreated(rs.getString("created"));
					return user;
				}
			});
		}

		public void deleteAll() throws RuntimeException {

			jdbcTemplate.update("DELETE FROM user");

		}



	
}
