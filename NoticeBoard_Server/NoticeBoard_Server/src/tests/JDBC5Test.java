package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import JDBC5.NoticeService;
import JDBC5.NoticeServiceProxy;
import JDBC5.NoticeServiceTarget;
import JDBC5.UserService5;
import JDBC5.advices.TxAdvise;
import dataclass.Notice;
import dataclass.User;

class JDBC5Test {

	public UserService5 userService;
	public NoticeService noticeService;

	DataSource dataSource;

	@BeforeEach
	public void initBefore() {

		// PlatformTransactionManager 생성 DI
		dataSource = new DataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/noticeboard");
		dataSource.setUsername("admin");
		dataSource.setPassword("admin");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		userService = new UserService5(jdbcTemplate, transactionManager);

		/*
		 * // Proxy 생성 // target과 proxy가 같은 인터페이스를 구현하고있다. NoticeServiceProxy
		 * noticeProxy = new NoticeServiceProxy(); noticeProxy.setNoticeService(new
		 * NoticeServiceTarget(jdbcTemplate));
		 * noticeProxy.setTransactionManager(transactionManager); noticeService =
		 * noticeProxy;
		 */

		// Dinamic Proxy 생성 
		// Dinamic Proxy는 구현체(target)를 알고있어야한다.
		NoticeService target = new NoticeServiceTarget(jdbcTemplate);
		noticeService = (NoticeService) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class<?>[] { NoticeService.class }, new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						TransactionStatus status = transactionManager
								.getTransaction(new DefaultTransactionDefinition());
						try {
							// Dinamic Proxy는구현체(target)를 알고있어야한다.
							Object result = method.invoke(target, args);
							transactionManager.commit(status);
							return result;
						} catch (RuntimeException e) {
							transactionManager.rollback(status);
							throw e;
						}
					}
				});

		/*
		 * // ProxyFactoryBean // ProxyFactoryBean은 구현체(target)를 내부적으로 가지고있다.
		 * ProxyFactoryBean factoryBean = new ProxyFactoryBean();
		 * factoryBean.setTarget(new NoticeServiceTarget(jdbcTemplate)); // Transaction
		 * advice MethodInterceptor advice = new TxAdvise(transactionManager); //
		 * pointCut updateNotices 함수이름 포인트컷 NameMatchMethodPointcut pointCut = new
		 * NameMatchMethodPointcut(); // 클래스 필터 : 모든 클래스 메소드 필터 : updateNotice 이름
		 * pointCut.setMappedName("updateNotices"); factoryBean.addAdvisor(new
		 * DefaultPointcutAdvisor(pointCut, advice)); noticeService = (NoticeService)
		 * factoryBean.getObject();
		 */
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
		Notice notice = new Notice();
		notice.setId(1);
		notice.setTitle("Test Title");
		notice.setAuthor("Test Author");
		notice.setBody("Test Body");

		// notice 추가
		noticeService.add(notice);
		// 추가한 notice 가져오기
		notice.setId(2);
		noticeService.add(notice);

		Notice notice1 = noticeService.get(1);
		Notice notice2 = noticeService.get(2);

		// 보관 기간 보다 긴 시간으로 임의로 세팅하기
		noticeService.update(notice1, "date", "1999-01-01 11:11:11");
		noticeService.update(notice2, "date", "a");
		assertEquals(2, noticeService.getCount());
		noticeService.updateNotices();
		// 보관기간이 지나 삭제되어야한다.
		assertEquals(0, noticeService.getCount());
	}

}
