package JDBC3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import JDBC.JdbcClose;
import dataclass.User;

public class UserService3 {

	UserDao3 userDao;

	public UserService3(JdbcConnection jdbcConnection) {
		userDao = new UserDao3(jdbcConnection);
	}

	public boolean add(String id, String pwd)  {
		try {
			userDao.add(id, pwd);
			return true;
		} catch (RuntimeException e) {
			return false;
		}
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
	boolean register(String id, String pwd) throws SQLException {
		userDao.add(id, pwd);
		if (userDao.get(id).getId().equals(id))
			return true;
		else
			return false;
	}

	boolean login(String id, String pwd) {
		try {
			User loginUser = userDao.get(id);
			// 비밀번호가 같은지 확인 결과 리턴
			return (loginUser.getPwd().equals(pwd));

		} catch (RuntimeException e) {
			// 로그인 실패
			return false;
		}
	}

}
