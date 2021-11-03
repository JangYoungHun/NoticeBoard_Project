package JDBC5;

import java.util.List;

import dataclass.Notice;

public interface NoticeService {


	public boolean add(Notice notice);

	public void update(Notice notice, String column, String value);
	public void update(Notice notice, String column, int value) ;

	public Notice get(int id);

	public int getCount();

	// DB���� ��� Notice �����´�.
	public List<Notice> getAll();

	public void deleteAll();

	public String readNotice();

	// �����Ⱓ�� ���� notice ����
	// Transaction ���� �ʿ�
	public void updateNotices() ;

}