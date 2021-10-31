package JDBC3;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dataclass.Notice;

public class NoticeService3 {

	NoticeDao3 noticeDao;
	// 전체 row count

	public NoticeService3(JdbcConnection jdbcConnection) {
		noticeDao = new NoticeDao3(jdbcConnection);
	}

	public boolean add(Notice notice) {
		// id값 : 전체 row수 + 1
		try {
			
			noticeDao.add(notice);
			return true;
		} catch (RuntimeException e) {
			return false;
		}

	}

	public Notice get(int id) {
		try {
			return noticeDao.get(id);
		} catch (RuntimeException e) {
			return null;
		}
	}

	public int getCount() {
		return noticeDao.getCount();
	}

	public List<Notice> getAll() {
		return noticeDao.getAll();
	}

	public void deleteAll() {
		noticeDao.deleteAll();
	}

	String readNotice() {
		List<Notice> notices;
		
		notices = noticeDao.getAll();
		
		if(notices == null)
			return "";
		
		// JSONObject jsonMain = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for (Notice notice : notices) {
			JSONObject obj = new JSONObject();
			obj.put("title", notice.getTitle());
			obj.put("author", notice.getAuthor());
			obj.put("body", notice.getBody());
			obj.put("date", notice.getDate());
			jsonArray.add(obj);
		}
		return jsonArray.toString();
	}
}