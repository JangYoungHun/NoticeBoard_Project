package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UserDao1 {

	private static UserDao1 instance;
	// mysql 주소
	private static final String SQL_URL = "jdbc:mysql://localhost:3306/noticeboard";
	private static final String SQL_ID = "admin";
	private static final String SQL_PWD = "admin";
	Connection connection;
	PreparedStatement statement;
	ResultSet resultSet;

	private UserDao1() {
		init_JDBC();
	}

	void init_JDBC() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(SQL_URL, SQL_ID, SQL_PWD);
			connection.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("DB연결 문제 발생");
		}
	}

	public static UserDao1 getInstance() {
		if (instance == null)
			instance = new UserDao1();
		return instance;
	}

	// 회원가입 -> Database 에 등록
	boolean register(String id, String pwd) {
		try {
			String query = "INSERT INTO user(id,pwd,created) VALUES(?,?,NOW())";
			statement = connection.prepareStatement(query);
			// password를 그대로 저장하지 말고 hashCode 로 변경하는 로직 차후 추가 필요
			statement.setString(1, id);
			statement.setString(2, pwd);
			statement.executeUpdate();
			connection.commit();
			return true;
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			return false;
		} finally {
			JdbcClose.close(statement);
		}
	}
	// id 중복확인
	public int isIdExist(String id) {
		try {
			String query = "SELECT id FROM USER WHERE BINARY id=?";
			statement = connection.prepareStatement(query);
			statement.setString(1, id);
			resultSet = statement.executeQuery();
			connection.commit();
			int result = -1;
			// 이미 동일한 id가 존재
			if (resultSet.next()) {
				result = 0;
			}
			// 해당 아이디가 존재하지 않는다 -> 생성 가능
			else {
				result = 1;
			}
			return result;
		} catch (Exception e) {

			try {
				connection.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			return -1;
		} finally {
			JdbcClose.close(statement);
			JdbcClose.close(resultSet);
		}
	}

	// 로그인 정보 확인
	int login(String id, String pwd) {

		try {
			String query = "SELECT pwd FROM user WHERE id=?";
			statement = connection.prepareStatement(query);
			// password를 그대로 저장하지 말고 hashCode 로 변경하는 로직 차후 추가 필요
			statement.setString(1, id);
			resultSet = statement.executeQuery();
			connection.commit();

			String password = "";
			if (resultSet.next()) {
				password = resultSet.getString("pwd");
			}

			if (password.equals(pwd)) {
				System.out.println("로그인 성공");
				return 1;

			} else {
				System.out.println("로그인 실패");
				return 0;
			}

		} catch (Exception e) {
			return -1;
		} finally {
			JdbcClose.close(statement);
			JdbcClose.close(resultSet);
		}
	}

	//메모 추가
	boolean addNotice(String title, String author, String body, String date) {
		try {
			
		String query = "INSERT INTO notices(title,author,body,date) VALUES(?,?,?,?)";
		statement = connection.prepareStatement(query);
		statement.setString(1, title);
		statement.setString(2, author);
		statement.setString(3, body);
		statement.setString(4, date);
		statement.executeUpdate();
		connection.commit();
		System.out.println("메모 추가 성공");
		return true;
		
		// password를 그대로 저장하지 말고 hashCode 로 변경하는 로직 차후 추가 필요
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("메모 추가 실패");
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
		finally {
			JdbcClose.close(statement);
		}
	}
	
	String readNotice(){

		try {
			
			String query = "SELECT title,author,body,date FROM notices";
			statement = connection.prepareStatement(query);
			
			resultSet = statement.executeQuery();
			connection.commit();
		//	JSONObject jsonMain = new JSONObject();
			JSONArray jsonArray = new JSONArray(); 
			
			while(resultSet.next()) {
				JSONObject obj = new JSONObject();
				
				obj.put("title",resultSet.getString("title"));
				obj.put("author",resultSet.getString("author"));
				obj.put("body",resultSet.getString("body"));
				obj.put("date",resultSet.getString("date"));
				jsonArray.add(obj);
			}
			
			
			return jsonArray.toString();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		finally {
			JdbcClose.close(resultSet);
			JdbcClose.close(statement);
		}
		
		return "";
	}
	
	
	
	public void destroy() {
		JdbcClose.close(connection);
		JdbcClose.close(statement);
		JdbcClose.close(resultSet);
	}

}
