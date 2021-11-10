
# NoticeBoard_Project
Notice Board App &amp; Spring Server

그룹 내에서 공지사항을 작성하고 공유할 수 있는 앱을 제작 하는 프로젝트.  
앱에서 로그인을 통해 사용자를 인증하고 인증된 구성원들끼리 공지사항을 공유할수 있다.  
사용자의 정보와 공지의 데이터는 Database 를 이용하여 관리한다.  
REST 방식을 사용하여 각 요청을 분리하, 요청에 맞는 처리를 하고 처리 결과를 반환하여 앱에서 결과에 따라 화면을 업데이트한다.   

## 사용한 기술 스택
### Programming Language
> + Java
> + kotlin

 
### Front-End (Android)
> - Navigation  
> - RecyclerView   
> - httpurlconnection  


### Back-End
> + Spring FrameWork
> + Mybatis
> + Mysql
> + Oracle DataBase
> + Rest




## 필요한 기능
### USER 관련
> + 사용자 회원가입 관련 기능 (id 중복확인, id 등록) 
> + 로그인 처리
> + 게시글을 수정 삭제 할 수 있는 권한 처리 (작성자만 수정, 삭제할 수 있다.) 
> 
### NOTICE 관련
> + 새로운 공지를 작성하고 추가하는 기능
> + 기존의 공지 사항을 수정하고 최종 수정 날짜를 업데이트하는 기능
> + 공지를 삭제하는 기능
> + 공지를 조회하는 기능


# APP
![포트폴리오-004](https://user-images.githubusercontent.com/81062639/140050235-d32e4334-5595-414e-8c69-69e0a3ae9bcc.png)
![005](https://user-images.githubusercontent.com/81062639/140045936-a6d0da49-7b50-419a-9fe9-effae8468c1c.png)
![006](https://user-images.githubusercontent.com/81062639/140045968-229fd883-6c8f-42f8-b2bf-73c226826913.png)
![007](https://user-images.githubusercontent.com/81062639/140045978-5f7421db-8d28-454d-a19a-1f32f9159143.png)
![008](https://user-images.githubusercontent.com/81062639/140045982-92c4a182-79fd-4ca7-b92b-df62f3439943.png)

## 웹서버 요청 
웹서버 요청을 하는 object HttpRequest, 매개변수로 Action을 받아 해당하는 로직을 수행하는 Thread 실행
요청에 필요한 parameter 에 따라 함수가 분류되고 정의 된다.

![캡처](https://user-images.githubusercontent.com/81062639/140474810-f29ec5ea-afaa-4fbd-92d7-1a6f97232e0b.PNG)


## 요청 ACTION 종류
### HttpRequest.action

![Action table](https://user-images.githubusercontent.com/81062639/140049557-59c7faf0-3dcd-4f04-9395-cc8448eed8c3.png)

## 요청 결과 종류
### HttpRequest.RequestResult

![HTTPrequest result](https://user-images.githubusercontent.com/81062639/141058153-fa6a9025-16ec-436b-a9a5-e920983042cd.png)

## Oracle DataBase 구조  
사용자 관련 데이터를 관리하는 User Table과 메모관련 데이터를 관리하는 Notice Table의 구성이다.  

![oracle](https://user-images.githubusercontent.com/81062639/140054802-8c3c1ace-e637-4279-b589-64e2de7d189d.PNG)  

각각의 Table에 매핑되는 코틀린의 DATA class.  


```kotlin

/* 사용자와 관련된 데이터 클래스
*   name : 사용자 이름
*   age : 사용자 연령
*   id : 사용자 id 
*   pwd : 사용자 password
*/
data class UserData(var name:String, var age:Int, var id:String, var pwd:String){}

/*  메모와 관련된 데이터 클래스 
*   id : 메모 구분 번호
*   title : 제목
*   author : 작성자 
*   body : 내용
*   date : 최종 수정일
*/
data class NoticeItem(var id: Int, var title:String, var author:String, val body:String, var date:String){}
```

# Server
## Notice_Server (구 버전)
#### spring을 사용하지 않고 자바 코드, 수동 DI를 사용.
한개의 서블릿이 요청을 받아 요청 타입에 따라 해당하는 함수를 호출하고 그결과를 반한한다.

5가지 버전의 프로젝트로 구성되어있다.
각각의 버전 수정관련 내용은 NoticeBoard_Server 수정 과정.txt 확인.

# Noticeboard_Spring
#### Spring FrameWork를 사용하여 기존의 서버를 Rest 방식으로 재구성한 서버.
#### 의도적으로 다양한 방식 사용.
ex)
> + Mybatis Mapper (xml, annotation),  
> + 요청 결과 (String 결과, ResponseEntity),  
> + DI (@Autowired, Lombok @Setter()),  
> + DataBase (MySQL,Oracle)


## REST URI
각 요청별 URI 와 요청 방식(HttpMethod)를 정의해 놓은 표.  


![rest uri](https://user-images.githubusercontent.com/81062639/140053856-b3741b07-4215-469e-9bb5-67401c735615.PNG)  

## User 관련 

### UserMapper
User DataBase에 조회,업데이트,추가,삭제 등의 작업 쿼리를 관리하는 Mybatis Mapper 어노테이션 설정.


```java
public interface UserMapper {
 
    // user 등록
	@Select("insert into userdata (id, pwd, name, age) "
			+ "values(#{id}, #{pwd}, #{name}, #{age})")
	public void add(User user) throws RuntimeException;
    
    // 해당 is  조회
	@Select("select * from userdata where id=#{id}")
	public User get(String id) throws RuntimeException;
    
    //전체 row 갯수 조회
	@Select("select count(*) from userdata")
	public int getCount() throws RuntimeException;
	
    //전체 row 조회
	@Select("select * from userdata")
	public List<User> getAll() throws RuntimeException;
	
    // user 
	@Delete("Delete from userdata")
	public void deleteAll() throws RuntimeException;
}
```
## UserController
유저와 관련된 데이터 Controller. 유저와 관련된 요청을 처리한다.  



```java
@Controller
@RequestMapping("/user/")
public class UserController {
	
	//User 관련 서비스 Interface
	@Setter(onMethod_ = {@Autowired})
	UserService userService;
	
	// 사용자 등록 Service
	@PostMapping("register")
	@ResponseBody
	public String register(@RequestBody User user) {
		return userService.register(user) ? "SUCCESS" : "FAILED";	
	}
	// 아이디 중복확인 Service
	@RequestMapping("idExist")
	@ResponseBody
	public String idExist(@RequestParam String id) {		
		return userService.isIdExist(id) ?  "ID_EXIST" : "ID_NOT_EXIST";	
	}
	// 로그인 정보확인 Service
	@PostMapping("login")
	@ResponseBody
	public ResponseEntity<User> login(@RequestBody User user) {	
		//로그인 성공시 사용자 정보 리턴
		return userService.login(user) 
				? new ResponseEntity<User>(userService.get(user.getId()), HttpStatus.OK) 
				: new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
	}	
}
```


## Notice 관련

### NoticeMapper
```xml
public interface NoticeMapper {	
    // 메모 추가
	public int add(Notice notice);	
	//메모 수정
    public int update(Notice notice);	
	//메모 삭제
    public int delete(int id); 
	//해당 id 메모 조회
    public Notice get(int id);		
	//전체 row 갯수 조회
    public int getCount();
	//전체 메모 조회
    public List<Notice> getAll() throws RuntimeException;	
	//전체 메모 삭제
    public void deleteAll();
}
```

### NoticeMapper.xml
Notice DataBase에 조회,업데이트,추가,삭제 등의 작업 쿼리를 관리하는 Mybatis Mapper 설정.


```java
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.spring.noticeboard.mapper.NoticeMapper">
    
    <!-- 해당 id 메모 조회-->
	<select id="get"
		resultType="com.spring.noticeboard.entity.Notice">
		select * from notice where id=#{id}
	</select>

     <!-- 전체 row 갯수 조회 -->
	<select id="getCount" resultType="int">
		select count(*) from notice
	</select>

    <!-- 전체 메모 조회-->
	<select id="getAll"
		resultType="com.spring.noticeboard.entity.Notice">
		select * from notice
	</select>

    <!-- 메모 추가 -->
	<insert id="add">
		insert into notice (id, title, author, body)
		values(seq_notice.nextval, #{title},#{author},#{body})
	</insert>

    <!-- 메모 수정 -->
	<update id="update">
		update notice set title=#{title},body=#{body},"date"=sysdate 
		where id = #{id}
	</update>
	
    <!-- 메모 삭제 -->
	<delete id="delete">
		delete from notice
		where id=#{id}
	</delete>
    
    <!-- 메모 전체 삭제 -->
	<delete id="deleteAll">
		delete from notice
	</delete>
</mapper>
```

### NoticeController
메오와 관련된 데이터 Controller. 메모와 관련된 요청을 처리하는 Controller.


```java
@RestController
@RequestMapping("/notice/")
public class NoticeController {
	
	@Autowired
	NoticeService noticeService;
	
	//메모 목록을 받아온다
	@RequestMapping("getNotices")
	private List<Notice> getNotices() {
		return noticeService.getAll();
	}
	
	//메모를 추가한다
	@PostMapping("addNotice")
	private String addNotice(@RequestBody Notice notice) {
		return noticeService.add(notice) ? "SUCCESS" : "FAILED";
	}
	
	// 해당하는 id의 메모를 제거 한다
	@DeleteMapping(value = "{id}")
	public void remove(@PathVariable int id) {
		noticeService.delete(id);
	}
	
	//해당 하는 id의 메모를 수정 한다
	@PutMapping(value = "{id}")
	public void update(@PathVariable("id")int id, @RequestBody Notice notice){		
		notice.setId(id);
		noticeService.update(notice);
	}
}

```
