package JDBC2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import JDBC.JdbcClose;
import dataclass.User;

public class UserService2 {
	
	UserDao2 userDao;
	
	//아이디 중복확인
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
		userDao.add(id,pwd);
		if(userDao.get(id).getId() == id)
			return true;
		else
			return false;
	}
	// 로그인 정보 확인
	boolean login(String id, String pwd) {
		try {
			User loginUser = userDao.get(id);
			//비밀번호가 같은지 확인 결과 리턴
			return (loginUser.getPwd().equals(pwd));
			
		} catch (RuntimeException e) {
			//로그인 실패
			return false;
		}
	}

	
	
	public UserService2() {
		userDao = new UserDao2();
	}

	public void add(String id, String pwd) throws RuntimeException {
		userDao.add(id, pwd);
	}		
	public User get(String id) throws RuntimeException {		
		return userDao.get(id);
	}

	public List<User> getAll() throws RuntimeException {	
		return userDao.getAll();
	}

	public void deleteAll() {
		userDao.deleteAll();
	}
	



	
}
