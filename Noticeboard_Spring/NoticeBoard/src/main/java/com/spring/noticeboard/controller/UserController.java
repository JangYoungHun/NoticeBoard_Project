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
	
	//User 관련 서비스 Interface
	@Setter(onMethod_ = {@Autowired})
	UserService userService;
	
	// 사용자 등록 Service
	@PostMapping("register")
	@ResponseBody
	public String register(@RequestBody User user) {
		return userService.register(user) ? "SUCCESS" : "FAILED";	
	}
	// 아이디 중복확인 Service
	@RequestMapping("idExist")
	@ResponseBody
	public String idExist(@RequestParam String id) {		
		return userService.isIdExist(id) ?  "ID_EXIST" : "ID_NOT_EXIST";	
	}
	
	// 로그인 정보확인 Service
	@PostMapping("login")
	@ResponseBody
	public ResponseEntity<User> login(@RequestBody User user) {	
		//로그인 성공시 사용자 정보 리턴
		return userService.login(user) 
				? new ResponseEntity<User>(userService.get(user.getId()), HttpStatus.OK) 
				: new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
