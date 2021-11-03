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

		// mysql �ּ�
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
				//DB�� �����ؼ� �Ķ���� id�� �ش��ϴ� ������ �д� ���� ����
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
			//DB�� �����ؼ� �Ķ���� id�� �ش��ϴ� ������ �д� ���� ����
			connection = getConnection(); 
			statement = connection.prepareStatement("SELECT * FROM user WHERE id=?");
			statement.setString(1, id);
			
			resultSet = statement.executeQuery();
			
			// �Ķ���ͷ� ���� id Colunm �о�� Notice ��ü ���� �Ͽ� ����
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
		
		//��ü row count 
		public int getCount() throws RuntimeException {		
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
		try {	
			//DB�� �����ؼ� �Ķ���� id�� �ش��ϴ� ������ �д� ���� ����
			connection = getConnection(); 
			statement = connection.prepareStatement("SELECT count(*) as count from user");
			resultSet = statement.executeQuery();
			
			// �Ķ���ͷ� ���� id Colunm �о�� Notice ��ü ���� �Ͽ� ����
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
			//DB�� �����ؼ� �Ķ���� id�� �ش��ϴ� ������ �д� ���� ����
			connection = getConnection(); 
			statement = connection.prepareStatement("SELECT * FROM user");
				
			resultSet = statement.executeQuery();
			//DB Column �о� ������ ����Ʈ
			List<User> users = new ArrayList<User>();
			// ��� Column �о�� List�� �߰�.
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
				//DB�� �����ؼ� �Ķ���� id�� �ش��ϴ� ������ �д� ���� ����
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
		
		
		//ResultSet�� �޾� Notice�� ����� ��ȯ���ִ� �Լ�
		User createUser(ResultSet resultSet) throws SQLException {
			User user = new User();
			
			user.setId(resultSet.getString("id"));
			user.setPwd(resultSet.getString("pwd"));
			user.setCreated(resultSet.getString("created"));
			return user;
		}

	

	
}