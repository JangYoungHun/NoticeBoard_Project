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


@Service
@Transactional(rollbackFor = Exception.class)
public class NoticeServiceImpl implements NoticeService {

	NoticeDao noticeDao;
	
	@Autowired
	public void setNoticeDao(NoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}
	// ��ü row count


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

	// DB���� ��� Notice �����´�.
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


	// �����Ⱓ�� ���� notice ����
	// Transaction ���� �ʿ�
	public void updateNotices() {
		List<Notice> notices = noticeDao.getAll();
		for (Notice notice : notices) {
			// ���� �Ⱓ Ȯ��
			if (notice.isOutdated()) {
				noticeDao.delete(notice.getId());
			}

		}

	}
}