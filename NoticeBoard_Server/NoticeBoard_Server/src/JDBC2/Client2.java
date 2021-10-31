package JDBC2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataclass.Notice;

@WebServlet("/Client2")
public class Client2 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	UserService2 userService;
	NoticeService2 noticeService;
	PrintWriter printWriter;

	//초기화
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		userService = new UserService2();
		noticeService = new NoticeService2();
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			request.setCharacterEncoding("UTF-8");
        	response.setContentType("text/html; charset=UTF-8");

		/*
		 * // 연결 테스트용 param String test = request.getParameter("test"); if(test!=null)
		 * System.out.println(test); else System.out.println("test = null"); //
		 */

		printWriter = new PrintWriter(response.getOutputStream());

		String requestType = request.getParameter("type");

		// MYSQL 쿼리
		String query = "";

		// 요청 확인
		try {
			if (requestType != null) {
				// MYSQL 연결 초기화
				switch (requestType) {
				case "isIdExist": {
					String id = request.getParameter("id");
					isExist(id);
				}
					break;
				case "register": {
					String id = request.getParameter("id");
					String pwd = request.getParameter("pwd");
					register(id, pwd);
				}
					break;
				case "login": {
					String id = request.getParameter("id");
					String pwd = request.getParameter("pwd");
					login(id, pwd);
				}
					break;
				case "addNotice": {
					String title = request.getParameter("title");
					String author = request.getParameter("author");
					String body = request.getParameter("body");
					String date = request.getParameter("date");
					addNotice(title,author,body,date);
				}
					break;
				case "readNotice": {
					readNotice();
				}
					break;
				}

			}
			else {
				// type parameter = 오류
				System.out.println("type parameter 요청 타입 오류");
				printWriter.write("SERVER_ERROR");
				printWriter.flush();
				printWriter.close();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("명령 처리 중 에러 발생");
		}
	}

	// id 중복확인
	void isExist(String id) throws SQLException {

		boolean exist = userService.isIdExist(id);
		// 0 : id 중복
		// 1 : id 중복X
		// -1 : Error
		String result = exist?"ID_EXIST":"ID_NOT_EXIST";

		printWriter.write(result);

		printWriter.flush();
		printWriter.close();

	}

	// 회원가입 -> Database 에 등록
	void register(String id, String pwd) throws SQLException {

		boolean requestResult = userService.register(id, pwd);
		String result = "";

		// 성공
		if (requestResult) {
			result = "SUCCESS";
		}
		// 실패
		else {
			result = "FAILED";
		}
		printWriter.write(result);
		printWriter.flush();
		printWriter.close();
	}

	//로그인 정보 확인 
	void login(String id, String pwd) throws SQLException {
		boolean requestResult = userService.login(id, pwd);
		// 0 : 로그인 실패
		// 1 : 로그인 설공
		// -1 : Error
		String result = "";		
		result = requestResult?"SUCCESS":"FAILED";		
		printWriter.write(result);
		printWriter.flush();
		printWriter.close();
	}

	//메모 추가
	private void addNotice(String title, String author, String body, String date) {	
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setBody(body);
		notice.setAuthor(author);
		boolean requestResult = noticeService.addNotice(notice);
		// true : 추가 성공
		// false : 추가 실패
		
		String result = "";
		
		result = requestResult ? "SUCCESS" : "FAILED";

		printWriter.write(result);

		printWriter.flush();
		printWriter.close();
	}
	//메모 정보 읽기
	private void readNotice() {
		try {
		String result = noticeService.readNotice();
		printWriter.write(URLEncoder.encode(result,"utf-8"));
		printWriter.flush();
		printWriter.close();
		}
		catch (Exception e) {
			printWriter.write("SERVER_ERROR");
		}
	}


}




