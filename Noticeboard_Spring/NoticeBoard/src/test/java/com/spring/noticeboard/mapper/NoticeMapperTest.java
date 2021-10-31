package com.spring.noticeboard.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.noticeboard.entity.Notice;
import com.spring.noticeboard.entity.User;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@Log4j
public class NoticeMapperTest {

	@Setter(onMethod_ = { @Autowired })
	NoticeMapper mapper;

	@Test
	public void testMapper() {
		log.info("test mapper not null ----------------");
		log.info("mapper : " + mapper);
	}

	@Test
	public void testAdd() {
		Notice notice = new Notice();
		notice.setAuthor("testId");
		notice.setBody("test body");
		notice.setTitle("test Title");
		mapper.add(notice);
	}
	
	@Test
	public void testGet() {
		log.info("test get ----------------");
		Notice notice = mapper.get(2);
		log.info(notice);
	}
	
	@Test
	public void testGetAll() {
		log.info("test get All----------------");
		List<Notice> list = mapper.getAll();
		for(Notice notice : list) {
			log.info(notice);
		}
	}
}
