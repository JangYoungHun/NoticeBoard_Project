package com.spring.noticeboard.service;

import java.util.List;

import com.spring.noticeboard.entity.User;

public interface UserService {


	public void add(User user);
	
	public User get(String id);

	public int getCount();

	public List<User> getAll();

	public void deleteAll();

	public boolean isIdExist(String id);
	// ȸ������ -> Database �� ���

	public boolean register(User user);

	public boolean login(User user);
}