package JDBC2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import JDBC.JdbcClose;
import dataclass.Notice;

public class NoticeService2 {
	
	NoticeDao2 noticeDao = new NoticeDao2();
	//메모 추가
	public boolean addNotice(Notice notice) throws RuntimeException {		
		// id값 : 전체 row수 + 1
		notice.setId(getCount()+1);		
		return noticeDao.add(notice);
	}
	// 메모 읽어오기
	String readNotice(){
		List<Notice> notices = noticeDao.getAll();
		//	JSONObject jsonMain = new JSONObject();
			JSONArray jsonArray = new JSONArray(); 		
			for(Notice notice : notices) {
				JSONObject obj = new JSONObject();
				obj.put("title",notice.getTitle());
				obj.put("author",notice.getAuthor());
				obj.put("body",notice.getBody());
				obj.put("date",notice.getDate());
				jsonArray.add(obj);
			}		
			return jsonArray.toString();
	}
	
	
	public Notice get(int id) throws RuntimeException {
		return noticeDao.get(id);
	}
	public int getCount() throws RuntimeException {		
		return noticeDao.getCount();
	}
	public List<Notice> getAll() throws RuntimeException {
		return noticeDao.getAll();
	}

	public void deleteAll() throws RuntimeException{
		noticeDao.deleteAll();
	}
	
	
	
}
