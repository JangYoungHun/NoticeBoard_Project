package com.spring.noticeboard.service;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.noticeboard.dao.NoticeDao;
import com.spring.noticeboard.dao.NoticeDaoImpl;
import com.spring.noticeboard.entity.Notice;

//@Service
@Transactional(rollbackFor = Exception.class)
public class NoticeServiceImpl implements NoticeService {

	NoticeDao noticeDao;
	
	@Autowired
	public void setNoticeDao(NoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}
	// 전체 row count


	public boolean add(Notice notice) {
		try {			
			noticeDao.add(notice);
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}

	@Override
	public void update(Notice notice) {
		noticeDao.update(notice);
		
	}

	public Notice get(int id) {
		try {
			return noticeDao.get(id);
		} catch (RuntimeException e) {
			return null;
		}
	}

	public int getCount() {
		return noticeDao.getCount();
	}

	// DB에서 모든 Notice 가져온다.
	public List<Notice> getAll() {
		return noticeDao.getAll();
	}
	
	@Override
	public void delete(int id) {
		noticeDao.delete(id);
		
	}
	public void deleteAll() {
		noticeDao.deleteAll();
	}


	// 보관기간이 지난 notice 제거
	// Transaction 관리 필요
	public void updateNotices() {
		List<Notice> notices = noticeDao.getAll();
		for (Notice notice : notices) {
			// 보관 기간 확인
			if (notice.isOutdated()) {
				noticeDao.delete(notice.getId());
			}

		}

	}
}