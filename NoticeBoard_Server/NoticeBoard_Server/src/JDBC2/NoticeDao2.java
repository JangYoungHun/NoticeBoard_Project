package JDBC2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import JDBC.JdbcClose;
import dataclass.Notice;

public class NoticeDao2 {

	// mysql �ּ�
	private static final String SQL_URL = "jdbc:mysql://localhost:3306/noticeboard";
	private static final String SQL_ID = "admin";
	private static final String SQL_PWD = "admin";
	
	Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");			
		 Connection connection = DriverManager.getConnection(SQL_URL, SQL_ID, SQL_PWD);
		
		return connection;
	}
	
	public boolean add(Notice notice)  {
		Connection connection = null;
		PreparedStatement statement = null;		
		try {
			//DB�� notice �߰�
			connection = getConnection();
			statement = connection.prepareStatement("INSERT INTO notices(id,title,author,body,date) "+
			"VALUES(?,?,?,?,NOW())");
			statement.setInt(1, notice.getId());
			statement.setString(2, notice.getTitle());
			statement.setString(3, notice.getAuthor());
			statement.setString(4, notice.getBody());		
			statement.executeUpdate();		
			return true;		
		} catch (Exception e) {
			return false;		
		}
		finally {
			JdbcClose.close(connection);
			JdbcClose.close(statement);
		}	
	}
	public Notice get(int id)  {
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
	try {	
		//DB�� �����ؼ� �Ķ���� id�� �ش��ϴ� ������ �д� ���� ����
		connection = getConnection();
		statement = connection.prepareStatement("SELECT * FROM notices WHERE id=?");
		statement.setInt(1, id);
		
		resultSet = statement.executeQuery();
		
		// �Ķ���ͷ� ���� id Colunm �о�� Notice ��ü ���� �Ͽ� ����
		resultSet.next();			
		return createNotice(resultSet);
		
	} catch (Exception e) {
		return null;
	}
	finally {
		JdbcClose.close(connection);
		JdbcClose.close(statement);
		JdbcClose.close(resultSet);
	}
	
	}
	public int getCount() throws RuntimeException {		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
	try {	
		//DB�� �����ؼ� �Ķ���� id�� �ش��ϴ� ������ �д� ���� ����
		connection = getConnection();
		statement = connection.prepareStatement("SELECT count(*) as count from notices");
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
	public List<Notice> getAll() throws RuntimeException {
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
	try {	
		//DB�� �����ؼ� �Ķ���� id�� �ش��ϴ� ������ �д� ���� ����
		connection = getConnection();
		statement = connection.prepareStatement("SELECT * FROM notices");
			
		resultSet = statement.executeQuery();
		//DB Column �о� ������ ����Ʈ
		List<Notice> notices = new ArrayList<Notice>();
		// ��� Column �о�� List�� �߰�.
		while(resultSet.next()) {			
			notices.add(createNotice(resultSet));
		}
		return notices;	
		
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	finally {
		JdbcClose.close(connection);
		JdbcClose.close(statement);
		JdbcClose.close(resultSet);
	}
		
	}
	public void deleteAll() throws RuntimeException{
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			//DB�� �����ؼ� �Ķ���� id�� �ش��ϴ� ������ �д� ���� ����
			connection = getConnection();
			statement = connection.prepareStatement("DELETE FROM notices");

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
	Notice createNotice(ResultSet resultSet) throws SQLException {
		Notice notice= new Notice();
		notice.setId(resultSet.getInt("id"));
		notice.setAuthor(resultSet.getString("author"));
		notice.setDate(resultSet.getString("date"));
		notice.setBody(resultSet.getString("body"));
		notice.setTitle(resultSet.getString("title"));
		return notice;
	}

	

}