package com.spring.noticeboard.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.spring.noticeboard.entity.Notice;



public interface NoticeService {


	public boolean add(Notice notice);

	public void update(Notice notice) ;

	public Notice get(int id);

	public int getCount();

	// DB에서 모든 Notice 가져온다.
	public List<Notice> getAll();

	public void delete(int id);
	public void deleteAll();

	// 보관기간이 지난 notice 제거
	// Transaction 관리 필요
	
	public void updateNotices() ;

}