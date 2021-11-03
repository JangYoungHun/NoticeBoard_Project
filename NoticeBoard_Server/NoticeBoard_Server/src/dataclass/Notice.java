package dataclass;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Notice {
	
	//�޸� ���� �Ⱓ ���� : ��
	private static final int DELETE_DAYS = 7;
	
	int id;
	String title;
	String author;
	String body;
	String date;
	
	
	// ���� �Ⱓ�� ���� �޸� ����
	//��� ������Ʈ�� ����
	public boolean isOutdated() {
		// ���� ��¥ YYYY-MM-DD��
		String nowDate = LocalDate.now().toString();
		String noticeDate = date.substring(0,10);
        
		//���� ���ڿ� notice ��¥ ���̸� ���Ѵ�.
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        // date1, date2 �� ��¥�� parse()�� ���� Date������ ��ȯ.
    
		try {
		     Date nowDateFormat= (Date) format.parse(nowDate);
			 Date noticeDateFormat = (Date) format.parse(noticeDate);
		        
		       // ms ������ ���� �Ͽ� ���� ����
		        long calDate = nowDateFormat.getTime() - noticeDateFormat.getTime(); 
		        
		        // ms �ʸ� ����Ͽ� �ϼ� ���
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