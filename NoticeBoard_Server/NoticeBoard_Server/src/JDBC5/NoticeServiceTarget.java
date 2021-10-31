package JDBC5;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import dataclass.Notice;

public class NoticeServiceTarget implements NoticeService {

	NoticeDao5 noticeDao;

	// 전체 row count
	public NoticeServiceTarget(JdbcTemplate jdbcTemplate) {
		this.noticeDao = new NoticeDao5(jdbcTemplate);
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

	public void update(Notice notice, String column, String value) {
		// DB에 notice 추가
		noticeDao.update(notice, column, value);
	}

	public void update(Notice notice, String column, int value) {
		// DB에 notice 추가
		noticeDao.update(notice, column, value);
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

	// DB에서 모든 Notice 가져온다.
	public List<Notice> getAll() {
		return noticeDao.getAll();
	}

	public void deleteAll() {
		noticeDao.deleteAll();
	}

	public String readNotice() {
		List<Notice> notices;

		notices = noticeDao.getAll();

		if (notices == null)
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

	// 보관기간이 지난 notice 제거
	// Transaction 관리 필요
	public void updateNotices() {
		List<Notice> notices = noticeDao.getAll();
		for (Notice notice : notices) {
			// 보관 기간 확인
			if (notice.isOutdated()) {
				noticeDao.delete(notice.getId());
			}

		}

	}
}