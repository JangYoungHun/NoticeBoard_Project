package com.spring.noticeboard.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.spring.noticeboard.entity.Notice;



public interface NoticeService {


	public boolean add(Notice notice);

	public void update(Notice notice) ;

	public Notice get(int id);

	public int getCount();

	// DB���� ��� Notice �����´�.
	public List<Notice> getAll();

	public void delete(int id);
	public void deleteAll();

	// �����Ⱓ�� ���� notice ����
	// Transaction ���� �ʿ�
	
	public void updateNotices() ;

}