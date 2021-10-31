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

public class UserDao2 {

		// mysql 주소
		private static final String SQL_URL = "jdbc:mysql://localhost:3306/noticeboard";
		private static final String SQL_ID = "admin";
		private static final String SQL_PWD = "admin";

		Connection getConnection() throws ClassNotFoundException, SQLException {
			Class.forName("com.mysql.cj.jdbc.Driver");			
			 Connection connection = DriverManager.getConnection(SQL_URL, SQL_ID, SQL_PWD);
			
			return connection;
		}
		
		public void add(String id, String pwd) throws RuntimeException {
			Connection connection = null;
			PreparedStatement statement = null;
			
			try {
				//DB에 접속해서 파라미터 id에 해당하는 데이터 읽는 쿼리 실행
				connection = getConnection(); 
				statement = connection.prepareStatement("INSERT INTO user(id,pwd,created) "+
				"VALUES(?,?,NOW())");
								
				statement.setString(1, id);
				statement.setString(2, pwd);

				statement.executeUpdate();
			} catch (Exception e) {		
				throw new RuntimeException(e);
			}
			finally {
				JdbcClose.close(connection);
				JdbcClose.close(statement);
			}
		
		}		
		public User get(String id) throws RuntimeException {		
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
		try {	
			//DB에 접속해서 파라미터 id에 해당하는 데이터 읽는 쿼리 실행
			connection = getConnection(); 
			statement = connection.prepareStatement("SELECT * FROM user WHERE id=?");
			statement.setString(1, id);
			
			resultSet = statement.executeQuery();
			
			// 파라미터로 받은 id Colunm 읽어와 Notice 객체 생성 하여 리턴
			resultSet.next();			
			return createUser(resultSet);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			JdbcClose.close(connection);
			JdbcClose.close(statement);
			JdbcClose.close(resultSet);
		}

			
		}
		
		//전체 row count 
		public int getCount() throws RuntimeException {		
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
		try {	
			//DB에 접속해서 파라미터 id에 해당하는 데이터 읽는 쿼리 실행
			connection = getConnection(); 
			statement = connection.prepareStatement("SELECT count(*) as count from user");
			resultSet = statement.executeQuery();
			
			// 파라미터로 받은 id Colunm 읽어와 Notice 객체 생성 하여 리턴
			resultSet.next();			
			return resultSet.getInt("count");
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			JdbcClose.close(connection);
			JdbcClose.close(statement);
			JdbcClose.close(resultSet);
		}

			
		}
		public List<User> getAll() throws RuntimeException {	
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
		try {	
			//DB에 접속해서 파라미터 id에 해당하는 데이터 읽는 쿼리 실행
			connection = getConnection(); 
			statement = connection.prepareStatement("SELECT * FROM user");
				
			resultSet = statement.executeQuery();
			//DB Column 읽어 저장할 리스트
			List<User> users = new ArrayList<User>();
			// 모든 Column 읽어와 List에 추가.
			while(resultSet.next()) {			
				users.add(createUser(resultSet));
			}
			return users;	
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			JdbcClose.close(connection);
			JdbcClose.close(statement);
			JdbcClose.close(resultSet);
		}			
		}
		
		
		
		
		public void deleteAll() {
			Connection connection = null;
			PreparedStatement statement = null;
			
			try {
				//DB에 접속해서 파라미터 id에 해당하는 데이터 읽는 쿼리 실행
				connection = getConnection(); 
				statement = connection.prepareStatement("DELETE FROM user");

				statement.executeUpdate();
			} catch (Exception e) {	
				throw new RuntimeException(e);
			}
			finally {
				JdbcClose.close(connection);
				JdbcClose.close(statement);
			}
		}
		
		
		//ResultSet을 받아 Notice를 만들어 반환해주는 함수
		User createUser(ResultSet resultSet) throws SQLException {
			User user = new User();
			
			user.setId(resultSet.getString("id"));
			user.setPwd(resultSet.getString("pwd"));
			user.setCreated(resultSet.getString("created"));
			return user;
		}

	

	
}
