package com.spring.noticeboard.controller;

import java.net.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.noticeboard.entity.User;
import com.spring.noticeboard.service.UserService;

import lombok.Setter;

@Controller
@RequestMapping("/user/")

public class UserController {
	
	
	//User ���� ���� Interface
	@Setter(onMethod_ = {@Autowired})
	UserService userService;
	
	// ����� ��� Service
	@PostMapping("register")
	@ResponseBody
	public String register(@RequestBody User user) {
		System.out.println("register-------------------------------------");
		return userService.register(user) ? "SUCCESS" : "FAILED";	
	}
	// ���̵� �ߺ�Ȯ�� Service
	@RequestMapping("idExist")
	@ResponseBody
	public String idExist(@RequestParam String id) {
		System.out.println("idExist-------------------------------------");
		return userService.isIdExist(id) ?  "ID_EXIST" : "ID_NOT_EXIST";	
	}
	
	// �α��� ����Ȯ�� Service
	@PostMapping("login")
	@ResponseBody
	public ResponseEntity<User> login(@RequestBody User user) {	
		//�α��� ������ ����� ���� ����
		return userService.login(user) 
				? new ResponseEntity<User>(userService.get(user.getId()), HttpStatus.OK) 
				: new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}