package com.spring.noticeboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.noticeboard.dao.UserDao;
import com.spring.noticeboard.dao.UserDaoImpl;
import com.spring.noticeboard.entity.User;

//@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService{

	UserDao userDao;

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void add(User user) {
		userDao.add(user);
	}

	public User get(String id) {
		try {
			return userDao.get(id);
		} catch (RuntimeException e) {
			return null;
		}
	}

	public int getCount() {
		return userDao.getCount();
	}

	public List<User> getAll() {
		try {
			return userDao.getAll();
		} catch (RuntimeException e) {
			return null;
		}
	}

	public void deleteAll() {
		userDao.deleteAll();
	}

	public boolean isIdExist(String id) {
		try {
			userDao.get(id);
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}

	// 회원가입 -> Database 에 등록
	public boolean register(User user) {
		try {
			userDao.add(user);
				return true;		
		} catch (Exception e) {
			return false;
		}

	}

	public boolean login(String id, String pwd) {
		try {
			User loginUser = userDao.get(id);
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
