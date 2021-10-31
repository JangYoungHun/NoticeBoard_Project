package com.spring.noticeboard.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.spring.noticeboard.entity.Notice;

public interface NoticeDao {
	
	public void add(Notice notice);
	public void update(Notice notice);
	
	public void delete(int id);
 
	public Notice get(int id);
		
	// ÀüÃ¼ row count
	public int getCount();

	public List<Notice> getAll() throws RuntimeException;
	public void deleteAll() ;

}
