package com.spring.noticeboard.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.noticeboard.entity.User;

@Repository
public class UserDaoImpl implements UserDao {

	
	JdbcTemplate jdbcTemplate;
		@Autowired
		public UserDaoImpl(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
		}
		
		public void add(User user) throws RuntimeException{
			// DB에 notice 추가
			jdbcTemplate.update("INSERT INTO user(name,age,id,pwd) "+"VALUES(?,?,?,?)",user.getName(),user.getAge(),user.getId(),user.getPwd());
		}
		
		public User get(String id) throws RuntimeException {
			return jdbcTemplate.queryForObject("SELECT * FROM user WHERE id=?", new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setAge(rs.getInt("age"));
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
					user.setName(rs.getString("name"));
					user.setAge(rs.getInt("age"));
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
