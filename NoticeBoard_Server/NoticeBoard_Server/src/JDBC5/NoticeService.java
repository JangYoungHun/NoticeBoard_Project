package JDBC5;

import java.util.List;

import dataclass.Notice;

public interface NoticeService {


	public boolean add(Notice notice);

	public void update(Notice notice, String column, String value);
	public void update(Notice notice, String column, int value) ;

	public Notice get(int id);

	public int getCount();

	// DB에서 모든 Notice 가져온다.
	public List<Notice> getAll();

	public void deleteAll();

	public String readNotice();

	// 보관기간이 지난 notice 제거
	// Transaction 관리 필요
	public void updateNotices() ;

}