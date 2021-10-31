package com.spring.noticeboard.dao;

import java.util.List;

import com.spring.noticeboard.entity.User;

public interface UserDao  {
		
		public void add(User user) throws RuntimeException;		
		public User get(String id) throws RuntimeException;
	
		public int getCount() throws RuntimeException;

		public List<User> getAll() throws RuntimeException;

		public void deleteAll() throws RuntimeException;

}
