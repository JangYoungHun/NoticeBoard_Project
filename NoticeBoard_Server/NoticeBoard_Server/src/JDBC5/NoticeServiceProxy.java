package JDBC5;

import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import dataclass.Notice;

public class NoticeServiceProxy implements NoticeService {
	
	//target (실제 로직 구현체) DI 
	NoticeService noticeService;
	//트랜잭션 관련 추상화 DI 
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

	
	//트랜잭션 기능을 사용하지 않는 메소드도 위임 해줘야만한다.
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
