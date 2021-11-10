package com.spring.noticeboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	//메모 목록을 받아온다
	@RequestMapping("getNotices")
	private List<Notice> getNotices() {
		return noticeService.getAll();
	}
	
	//메모를 추가한다
	@PostMapping("addNotice")
	private ResponseEntity<String> addNotice(@RequestBody Notice notice) {
		return noticeService.add(notice) ?	
				new ResponseEntity<String>(HttpStatus.OK) 
				: new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	// 해당하는 id의 메모를 제거 한다
	@DeleteMapping(value = "{id}")
	public void remove(@PathVariable int id) {
		noticeService.delete(id);
	}
	
	//해당 하는 id의 메모를 수정 한다
	@PutMapping(value = "{id}")
	public void update(@PathVariable("id")int id, @RequestBody Notice notice){		
		notice.setId(id);
		noticeService.update(notice);
	}
}

