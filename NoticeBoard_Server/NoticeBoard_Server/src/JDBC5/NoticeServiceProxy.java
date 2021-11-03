package JDBC5;

import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import dataclass.Notice;

public class NoticeServiceProxy implements NoticeService {
	
	//target (���� ���� ����ü) DI 
	NoticeService noticeService;
	//Ʈ����� ���� �߻�ȭ DI 
	PlatformTransactionManager transactionManager;
	
	@Override
	public void updateNotices() {
	
		TransactionStatus status =transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			noticeService.readNotice();
			transactionManager.commit(status);
		}catch (Exception e) {
			transactionManager.rollback(status);		
		}	

	}

	
	//Ʈ����� ����� ������� �ʴ� �޼ҵ嵵 ���� ����߸��Ѵ�.
	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	@Override
	public boolean add(Notice notice) {		
		return noticeService.add(notice);
	}

	@Override
	public void update(Notice notice, String column, String value) {
		noticeService.update(notice, column, value);
	}

	@Override
	public void update(Notice notice, String column, int value) {
		noticeService.update(notice, column, value);
	}

	@Override
	public Notice get(int id) {		
		return noticeService.get(id);
	}

	@Override
	public int getCount() {	
		return noticeService.getCount();
	}

	@Override
	public List<Notice> getAll() {	
		return noticeService.getAll();
	}

	@Override
	public void deleteAll() {
		noticeService.deleteAll();
	}

	@Override
	public String readNotice() {	
		return noticeService.readNotice();
	}


}