package com.spring.noticeboard.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.noticeboard.entity.Notice;

@Repository
public class NoticeDaoImpl implements NoticeDao{

	JdbcTemplate jdbcTemplate;
	
	@Autowired
	public NoticeDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void add(Notice notice) {
		// DB에 notice 추가
		jdbcTemplate.update("INSERT INTO notices(id,title,author,body)" + "VALUES(?,?,?,?)"
		,notice.getId(), notice.getTitle(), notice.getAuthor(), notice.getBody());
	}
	
	public void update(Notice notice) {
		// DB에 notice 추가
		jdbcTemplate.update("UPDATE notices SET title=?, body=?, date=NOW() where id=?" ,notice.getTitle(),notice.getBody(),notice.getId());	
	}
	
	public void delete(int id) {
		// DB에 notice 추가
		jdbcTemplate.update("DELETE FROM notices WHERE id=?"
		,id);
	}
 
	public Notice get(int id) {
		return jdbcTemplate.queryForObject("SELECT * FROM notices WHERE id=?", new RowMapper<Notice>() {
			@Override
			public Notice mapRow(ResultSet rs, int rowNum) throws SQLException {
				Notice notice = new Notice();
				notice.setId(rs.getInt("id"));
				notice.setAuthor(rs.getString("author"));
				notice.setDate(rs.getString("date"));
				notice.setBody(rs.getString("body"));
				notice.setTitle(rs.getString("title"));
				return notice;
			}
		}, id);						
	}
		
	// 전체 row count
	public int getCount(){
		return jdbcTemplate.queryForObject("SELECT count(*) as count from notices", new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("count");
			}
		});
	}

	public List<Notice> getAll() throws RuntimeException{
		return jdbcTemplate.query("SELECT * FROM notices", new RowMapper<Notice>() {
			@Override
			public Notice mapRow(ResultSet rs, int rowNum) throws SQLException {
				Notice notice = new Notice();
				notice.setId(rs.getInt("id"));
				notice.setAuthor(rs.getString("author"));
				notice.setDate(rs.getString("date"));
				notice.setBody(rs.getString("body"));
				notice.setTitle(rs.getString("title"));
				return notice;
			}
		});
	
	}
	public void deleteAll() {
		jdbcTemplate.update("DELETE FROM notices"); 
	}


}
