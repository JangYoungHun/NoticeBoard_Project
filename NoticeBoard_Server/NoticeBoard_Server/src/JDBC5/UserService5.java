package JDBC5;

import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import dataclass.User;

public class UserService5 {

	UserDao5 userDao;
	PlatformTransactionManager transactionManager;
	public UserService5(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
		userDao = new UserDao5(jdbcTemplate);
		this.transactionManager = transactionManager;
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

	// ȸ������ -> Database �� ���
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
			// ��й�ȣ�� ������ Ȯ�� ��� ����
			return (loginUser.getPwd().equals(pwd));

		} catch (RuntimeException e) {
			// �α��� ����
			return false;
		}
	}

}