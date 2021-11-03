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

	// ��ü row count
	public NoticeService4(JdbcTemplate jdbcTemplate,PlatformTransactionManager transactionManager) {
		this.noticeDao = new NoticeDao4(jdbcTemplate);
		this.transactionManager = transactionManager;
	}


	public boolean add(Notice notice) {
		// id�� : ��ü row�� + 1
		try {
			notice.setId(getCount() + 1);
			noticeDao.add(notice);
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}

	public void update(Notice notice, String column, String value) {
		// DB�� notice �߰�
		noticeDao.update(notice, column, value);
	}

	public void update(Notice notice, String column, int value) {
		// DB�� notice �߰�
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

	// DB���� ��� Notice �����´�.
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

	// �����Ⱓ�� ���� notice ����
	// ��ȸ�� ���� �ΰ��� �۾��� �� Ʈ��������� �������Ѵ�.
	// Transaction ���� �ʿ�
	public void updateNotices() {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		// �޸� List ��ȸ
			List<Notice> notices = noticeDao.getAll();
			for (Notice notice : notices) {
				// ���� �Ⱓ Ȯ��
				if (notice.isOutdated()) {
					//���� �Ⱓ�� �������� ����
					noticeDao.delete(notice.getId());
				}
			}
			transactionManager.commit(status);
	}

}