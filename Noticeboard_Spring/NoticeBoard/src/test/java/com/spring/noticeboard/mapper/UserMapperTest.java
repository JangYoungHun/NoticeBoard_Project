package com.spring.noticeboard.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.noticeboard.entity.User;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@Log4j
public class UserMapperTest {

	@Setter(onMethod_ = { @Autowired })
	UserMapper mapper;

	@Test
	public void testMapper() {
		log.info("test mapper noy null ----------------");
		log.info("mapper : " + mapper);
	}

	@Test
	public void testAdd() {
		User user = new User();
		user.setId("testId");
		user.setPwd("testPwd");
		user.setName("testName");
		user.setAge(20);
		
		mapper.add(user);
		
		User user1 = mapper.get("testId");
		
		log.info(user1);
	}
}
