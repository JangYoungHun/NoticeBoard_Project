package com.spring.noticeboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.spring.noticeboard.entity.Notice;
import com.spring.noticeboard.entity.User;
import com.spring.noticeboard.service.NoticeService;
import com.spring.noticeboard.service.UserService;
import com.spring.noticeboard.service.UserServiceImpl;
import com.zaxxer.hikari.HikariDataSource;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class JdbcTest {

	@Autowired
	public UserService userService;
	@Autowired
	public NoticeService noticeService;
	@Autowired
	HikariDataSource dataSource;

	
	@Test
	public void testUserDaoAdd() {
		User user = new User();
		String id = "testID";
		String pwd = "testPWD";
		user.setId(id);
		user.setName("name");
		user.setPwd(pwd);
		user.setAge(12);
		userService.deleteAll();
		assertTrue(userService.getCount() == 0);
		userService.add(user);
		assertTrue(userService.getCount() == 1);
		User user1 = userService.get(id);
		assertEquals(user1.getId(), id);
		assertEquals(user1.getPwd(), pwd);
	}

	// DB notices Table에 데이터 추가 기능 테스트

	@Test
	public void testNoticeDaoAdd() {
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

	public void checkSameNotice(Notice notice1, Notice notice2) {
		assertEquals(notice1.getBody(), notice2.getBody());
		assertEquals(notice1.getId(), notice2.getId());
		assertEquals(notice1.getTitle(), notice2.getTitle());
		assertEquals(notice1.getAuthor(), notice2.getAuthor());
	}
	
	

	
	
}
