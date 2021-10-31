package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.tomcat.jdbc.pool.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import JDBC3.JdbcConnection;
import JDBC3.NoticeService3;
import JDBC3.UserService3;
import JDBC4.NoticeService4;
import JDBC4.UserService4;
import dataclass.Notice;
import dataclass.User;

class JDBC4Test {

	public UserService4 userService;
	public NoticeService4 noticeService;

	DataSource dataSource;

	@BeforeEach
	public void initBefore() {

		dataSource = new DataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/noticeboard");
		dataSource.setUsername("admin");
		dataSource.setPassword("admin");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		userService = new UserService4(jdbcTemplate, transactionManager);
		noticeService = new NoticeService4(jdbcTemplate, transactionManager);

	}

	// DB user Table에 데이터 추가 기능 테스트
	@org.junit.jupiter.api.Test
	void testUserDaoAdd() {
		String id = "testID";
		String pwd = "testPWD";
		userService.deleteAll();
		assertTrue(userService.getCount() == 0);
		userService.add(id, pwd);
		assertTrue(userService.getCount() == 1);
		User user = userService.get(id);
		assertEquals(user.getId(), id);
		assertEquals(user.getPwd(), pwd);
	}

	// DB notices Table에 데이터 추가 기능 테스트
	@org.junit.jupiter.api.Test
	void testNoticeDaoAdd() {
		noticeService.deleteAll();
		Notice notice1 = new Notice();
		notice1.setId(1);
		notice1.setTitle("Test Title");
		notice1.setAuthor("Test Author");
		notice1.setBody("Test Body");
		notice1.setDate("Test Date");

		noticeService.add(notice1);

		Notice notice2 = noticeService.get(notice1.getId());
		checkSameNotice(notice1, notice2);
	}

	void checkSameNotice(Notice notice1, Notice notice2) {
		assertEquals(notice1.getBody(), notice2.getBody());
		assertEquals(notice1.getId(), notice2.getId());
		assertEquals(notice1.getTitle(), notice2.getTitle());
		assertEquals(notice1.getAuthor(), notice2.getAuthor());
	}

	@org.junit.jupiter.api.Test
	void testUpdateNoticeList() {
		noticeService.deleteAll();
		Notice notice1 = new Notice();
		notice1.setId(1);
		notice1.setTitle("Test Title");
		notice1.setAuthor("Test Author");
		notice1.setBody("Test Body");

		// notice 추가
		noticeService.add(notice1);
		// 추가한 notice 가져오기
		Notice notice = noticeService.get(1);

		// 보관 기간 보다 긴 시간으로 임의로 세팅하기
		noticeService.update(notice, "date", "1999-01-01 11:11:11");
		assertEquals(1, noticeService.getCount());
		noticeService.updateNotices();
		// 보관기간이 지나 삭제되어야한다.
		assertEquals(0, noticeService.getCount());
	}

	@org.junit.jupiter.api.Test void testRollBackUpdateNoticeList() {
	  
	  noticeService.deleteAll(); Notice notice = new Notice(); notice.setId(1);
	  notice.setTitle("Test Title"); notice.setAuthor("Test Author");
	  notice.setBody("Test Body");
	  
	  // notice 추가
	  noticeService.add(notice); 
	  notice.setId(2);
	  // notice 추가
	  noticeService.add(notice);
	  
	  // 추가한 notice 가져오기
	  Notice notice1 = noticeService.get(1);
	  Notice notice2 =noticeService.get(2); // 보관 기간 보다 긴 시간으로 임의로 세팅하기
	  noticeService.update(notice1,"date","1999-01-01 11:11:11");
	  
	  //잘못된 date 형식으로 값 세팅
	  noticeService.update(notice2,"date","1999-011 11:11:11");
	  //add 2개 정상적으로 완료되었는지 확인 
	  assertEquals(2, noticeService.getCount());
	  noticeService.updateNotices();
	  // notice1 삭제 후 notice2 삭제시 에러발생 rollback되어야 한다.
	  assertEquals(2, noticeService.getCount());
	  }

}
