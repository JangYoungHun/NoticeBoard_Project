package com.spring.noticeboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.noticeboard.entity.User;
import com.spring.noticeboard.mapper.UserMapper;

import lombok.Setter;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceMybatis implements UserService{

	@Setter(onMethod_ = {@Autowired})
	UserMapper mapper;


	public void add(User user) {
		mapper.add(user);
	}

	public User get(String id) {
		try {
			return mapper.get(id);
		} catch (RuntimeException e) {
			return null;
		}
	}

	public int getCount() {
		return mapper.getCount();
	}

	public List<User> getAll() {
		try {
			return mapper.getAll();
		} catch (RuntimeException e) {
			return null;
		}
	}

	public void deleteAll() {
		mapper.deleteAll();
	}

	public boolean isIdExist(String id) {
			
			return mapper.get(id) != null;
	}

	// 회원가입 -> Database 에 등록
	public boolean register(User user) {
		try {
			mapper.add(user);
				return true;		
		} catch (Exception e) {
			return false;
		}

	}

	public boolean login(String id, String pwd) {
		try {
			User loginUser = mapper.get(id);
			// 비밀번호가 같은지 확인 결과 리턴
			return (loginUser.getPwd().equals(pwd));

		} catch (RuntimeException e) {
			// 로그인 실패
			return false;
		}
	}
	
	@Override
	public boolean login(User user) {
		return login(user.getId(), user.getPwd());
	}



}
