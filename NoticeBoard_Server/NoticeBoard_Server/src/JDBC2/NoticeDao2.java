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

	// mysql 주소
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
			//DB에 notice 추가
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
		//DB에 접속해서 파라미터 id에 해당하는 데이터 읽는 쿼리 실행
		connection = getConnection();
		statement = connection.prepareStatement("SELECT * FROM notices WHERE id=?");
		statement.setInt(1, id);
		
		resultSet = statement.executeQuery();
		
		// 파라미터로 받은 id Colunm 읽어와 Notice 객체 생성 하여 리턴
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
		//DB에 접속해서 파라미터 id에 해당하는 데이터 읽는 쿼리 실행
		connection = getConnection();
		statement = connection.prepareStatement("SELECT count(*) as count from notices");
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
	public List<Notice> getAll() throws RuntimeException {
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
	try {	
		//DB에 접속해서 파라미터 id에 해당하는 데이터 읽는 쿼리 실행
		connection = getConnection();
		statement = connection.prepareStatement("SELECT * FROM notices");
			
		resultSet = statement.executeQuery();
		//DB Column 읽어 저장할 리스트
		List<Notice> notices = new ArrayList<Notice>();
		// 모든 Column 읽어와 List에 추가.
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
			//DB에 접속해서 파라미터 id에 해당하는 데이터 읽는 쿼리 실행
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
	//ResultSet을 받아 Notice를 만들어 반환해주는 함수
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
