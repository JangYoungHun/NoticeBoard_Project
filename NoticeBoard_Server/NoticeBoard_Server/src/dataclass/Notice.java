package dataclass;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Notice {
	
	//메모 보관 기간 단위 : 일
	private static final int DELETE_DAYS = 7;
	
	int id;
	String title;
	String author;
	String body;
	String date;
	
	
	// 일정 기간이 지난 메모 삭제
	//모든 오브젝트가 공유
	public boolean isOutdated() {
		// 현재 날짜 YYYY-MM-DD’
		String nowDate = LocalDate.now().toString();
		String noticeDate = date.substring(0,10);
        
		//현재 날자와 notice 날짜 차이를 구한다.
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
    
		try {
		     Date nowDateFormat= (Date) format.parse(nowDate);
			 Date noticeDateFormat = (Date) format.parse(noticeDate);
		        
		       // ms 단위로 변경 하여 차를 구함
		        long calDate = nowDateFormat.getTime() - noticeDateFormat.getTime(); 
		        
		        // ms 초를 사용하여 일수 계산
		        long days = calDate / ( 24*60*60*1000); 
		 
		       days = Math.abs(days);
		       
		       if(days >= DELETE_DAYS)
		    	   return true;
		       
		       return false;
			
		} catch (ParseException e) {
			return true;
		}
      


	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	
}
