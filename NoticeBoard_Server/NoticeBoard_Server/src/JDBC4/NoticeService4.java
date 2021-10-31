package JDBC4;

import java.text.ParseException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import dataclass.Notice;

public class NoticeService4 {

	PlatformTransactionManager transactionManager;
	NoticeDao4 noticeDao;

	// 전체 row count
	public NoticeService4(JdbcTemplate jdbcTemplate,PlatformTransactionManager transactionManager) {
		this.noticeDao = new NoticeDao4(jdbcTemplate);
		this.transactionManager = transactionManager;
	}


	public boolean add(Notice notice) {
		// id값 : 전체 row수 + 1
		try {
			notice.setId(getCount() + 1);
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
	// 조회와 제거 두가지 작업이 한 트랜잭션으로 묶여야한다.
	// Transaction 관리 필요
	public void updateNotices() {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		// 메모 List 조회
			List<Notice> notices = noticeDao.getAll();
			for (Notice notice : notices) {
				// 보관 기간 확인
				if (notice.isOutdated()) {
					//보관 기간이 지났으면 삭제
					noticeDao.delete(notice.getId());
				}
			}
			transactionManager.commit(status);
	}

}