package com.spring.noticeboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.noticeboard.entity.Notice;
import com.spring.noticeboard.mapper.NoticeMapper;

import lombok.Setter;

@Service
@Transactional(rollbackFor = Exception.class)
public class NoticeServiceMybatis implements NoticeService {
	
	@Setter(onMethod_ = {@Autowired})
	NoticeMapper mapper;

	@Override
	public boolean add(Notice notice) {
		try {			
			mapper.add(notice);
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}

	@Override
	public void update(Notice notice) {
		mapper.update(notice);	
	}

	@Override
	public Notice get(int id) {
		try {
			return mapper.get(id);
		} catch (RuntimeException e) {
			return null;
		}
	}

	@Override
	public int getCount() {
		return mapper.getCount();
	}

	@Override
	public List<Notice> getAll() {
		return mapper.getAll();
	}

	@Override
	public void delete(int id) {
		mapper.delete(id);
	}

	@Override
	public void deleteAll() {
		mapper.deleteAll();	
	}
	@Override
	public void updateNotices(){
		List<Notice> notices =mapper.getAll();
		for (Notice notice : notices) {
			// 보관 기간 확인
			if (notice.isOutdated()) {
				mapper.delete(notice.getId());
			}
		}
	}
 
}
