package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.tomcat.jdbc.pool.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.JdbcTemplate;

import JDBC3.JdbcConnection;
import JDBC3.NoticeService3;
import JDBC3.UserService3;
import JDBC4.NoticeService4;
import JDBC4.UserService4;
import dataclass.Notice;
import dataclass.User;

class JDBC3Test {

	
	  public UserService3 userService;
	  public NoticeService3 noticeService;
	 


	 
	DataSource dataSource;

	@BeforeEach
	public void initBefore() {

		dataSource = new DataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/noticeboard");
		dataSource.setUsername("admin");
		dataSource.setPassword("admin");
		// JDBC3 테스트용
		
		  JdbcConnection<User> jdbcConnection1 = new JdbcConnection<User>(dataSource);
		  JdbcConnection<Notice> jdbcConnection2 = new
		  JdbcConnection<Notice>(dataSource); userService = new
		  UserService3(jdbcConnection1); noticeService = new
		  NoticeService3(jdbcConnection2);
		 

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
		assertTrue(noticeService.getCount() == 1);
		Notice notice2 = noticeService.get(notice1.getId());
		checkSameNotice(notice1, notice2);
	}

	void checkSameNotice(Notice notice1, Notice notice2) {
		assertEquals(notice1.getBody(), notice2.getBody());
		assertEquals(notice1.getId(), notice2.getId());
		assertEquals(notice1.getTitle(), notice2.getTitle());
		assertEquals(notice1.getAuthor(), notice2.getAuthor());
	}

}
