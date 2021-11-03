package com.spring.noticeboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.noticeboard.entity.Notice;
import com.spring.noticeboard.service.NoticeService;

@RestController
@RequestMapping("/notice/")
public class NoticeController {
	
	@Autowired
	NoticeService noticeService;
	
	//�޸� ����� �޾ƿ´�.
	@RequestMapping("getNotices")
	private List<Notice> getNotices() {
		return noticeService.getAll();
	}
	
	//�޸� �߰��Ѵ�.
	@PostMapping("addNotice")
	private String addNotice(@RequestBody Notice notice) {
		return noticeService.add(notice) ? "SUCCESS" : "FAILED";
	}
	
	// �ش��ϴ� id�� �޸� ���� �Ѵ�.
	@DeleteMapping(value = "{id}")
	public void remove(@PathVariable int id) {
		noticeService.delete(id);
	}
	
	//�ش� �ϴ� id�� �޸� ���� �Ѵ�.
	@PutMapping(value = "{id}")
	public void update(@PathVariable("id")int id, @RequestBody Notice notice){		
		notice.setId(id);
		noticeService.update(notice);
	}
}