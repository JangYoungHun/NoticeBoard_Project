package com.spring.noticeboard.mapper;

import java.util.List;

import com.spring.noticeboard.dao.NoticeDao;
import com.spring.noticeboard.entity.Notice;

public interface NoticeMapper {
	
	public int add(Notice notice);
	
	public int update(Notice notice);
	
	public int delete(int id);
 
	public Notice get(int id);
		
	public int getCount();

	public List<Notice> getAll() throws RuntimeException;
	
	public void deleteAll();
}
