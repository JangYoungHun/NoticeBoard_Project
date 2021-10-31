package com.spring.noticeboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.spring.noticeboard.dao.UserDao;
import com.spring.noticeboard.entity.User;

public interface UserMapper {

	@Select("insert into userdata (id, pwd, name, age) "
			+ "values(#{id}, #{pwd}, #{name}, #{age})")
	
	public void add(User user) throws RuntimeException;

	@Select("select * from userdata where id=#{id}")
	public User get(String id) throws RuntimeException;

	@Select("select count(*) from userdata")
	public int getCount() throws RuntimeException;
	
	@Select("select * from userdata")
	public List<User> getAll() throws RuntimeException;
	
	@Delete("Delete from userdata")
	public void deleteAll() throws RuntimeException;
}
