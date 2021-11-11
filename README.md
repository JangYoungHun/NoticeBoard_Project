
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
> + 새로운 메모를 작성하고 추가하는 기능
> + 기존의 메모를 수정하고 최종 수정 날짜를 업데이트하는 기능
> + 메모를 삭제하는 기능
> + 메모를 조회하는 기능


# APP
![포트폴리오-004](https://user-images.githubusercontent.com/81062639/140050235-d32e4334-5595-414e-8c69-69e0a3ae9bcc.png)
![005](https://user-images.githubusercontent.com/81062639/140045936-a6d0da49-7b50-419a-9fe9-effae8468c1c.png)
![006](https://user-images.githubusercontent.com/81062639/140045968-229fd883-6c8f-42f8-b2bf-73c226826913.png)
![007](https://user-images.githubusercontent.com/81062639/140045978-5f7421db-8d28-454d-a19a-1f32f9159143.png)
![008](https://user-images.githubusercontent.com/81062639/140045982-92c4a182-79fd-4ca7-b92b-df62f3439943.png)


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


## MyBatis
사용자와 메모 DataBase에 접속하여 CRUD 처리를 Mybatis를 사용하여 처리 했습니다.

### UserMapper
사용자 관련 DabaBase 
어노테이션을 사용하여 작성

```java

public interface UserMapper {
	
	// 사용자를 추가한다.
	@Select("insert into userdata (id, pwd, name, age) "
			+ "values(#{id}, #{pwd}, #{name}, #{age})")	
	public void add(User user) throws RuntimeException;

	// 해당하는 id의 사용자 정보를 조회한다.
	@Select("select * from userdata where id=#{id}")
	public User get(String id) throws RuntimeException;

	// 전체 유저의 수를 조회한다.
	@Select("select count(*) from userdata")
	public int getCount() throws RuntimeException;
	
	// 전체 유저 정보를 조회한다.
	@Select("select * from userdata")
	public List<User> getAll() throws RuntimeException;
	
	// 체 유저정보를 삭제한다.
	@Delete("Delete from userdata")
	public void deleteAll() throws RuntimeException;
}

```
### NoticeMapper
메모 관련 DabaBase   
어노테이션을 사용하지않고 xml을 사용  

```java
public interface NoticeMapper {
	
	// 메모를 추가한다.
	public int add(Notice notice);
	
	//메모를 수정한다.
	public int update(Notice notice);
	
	// 해당하는 id의 메모를 삭제한다.
	public int delete(int id);
 	
	// 해당하는 id의 메모 정보를 조회한다.
	public Notice get(int id);
	
	//전체 메모의 갯수를 조회한다.	
	public int getCount();
	
	//전체 메모의 목록을 조회한다.
	public List<Notice> getAll() throws RuntimeException;
	
	// 메모를 전부 삭제한다.
	public void deleteAll();
}
```

### NoticeMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.spring.noticeboard.mapper.NoticeMapper">

	<select id="get"
		resultType="com.spring.noticeboard.entity.Notice">
		select * from notice where id=#{id}
	</select>

	<select id="getCount" resultType="int">
		select count(*) from notice
	</select>

	<select id="getAll"
		resultType="com.spring.noticeboard.entity.Notice">
		select * from notice order by "date" DESC
	</select>

	<insert id="add">
		insert into notice (id, title, author, body)
		values(seq_notice.nextval, #{title},#{author},#{body})
	</insert>

	<update id="update">
		update notice set title=#{title},body=#{body},"date"=sysdate 
		where id = #{id}
	</update>
	
	<delete id="delete">
		delete from notice
		where id=#{id}
	</delete>

	<delete id="deleteAll">
		delete from notice
	</delete>
</mapper>
```

## REST URI
각 요청별 URI 와 요청 방식(HttpMethod)를 정의해 놓은 표.  

![rest uri](https://user-images.githubusercontent.com/81062639/140053856-b3741b07-4215-469e-9bb5-67401c735615.PNG)  


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


## 회원 가입

![회원가입 화면](https://user-images.githubusercontent.com/81062639/141233090-9688edd8-232f-44b8-afd8-c0e35590b0aa.PNG)



### ID 중복 확인 요청
회원 가입 화면에서 원하는 id를 입력하고 id 중복확인 버튼을 누르면 해당 id 의 존재 여부를 확인하기 위해  
HttpRequest의 checkIdExist()를 호출하여 서버에 요청을 보낸다.     
요청을 받은 서버는 User DataBase를 조회하여 중복 확인 결과를 반환한다.  

#### 회원 가입 화면 중복확인 버튼 
```kotlin

  v = inflater.inflate(R.layout.fragment_create_account, container, false)

        //아이디 중복확인 버튼
        v.btn_idCheck.setOnClickListener {
	
	   // 현재 입력한 id (중복확인할 id) 
            val idInput =v.editText_id.text.toString()

            // 입력한 id가 올바른 형식으로 작성되었는지 확인.
            if(idValidity(idInput)) {
		
		//id 중복확인 요청 
                when (HttpRequest.checkIdExist(id = idInput)) {
	            // 사용가능한 id인 경우
                    HttpRequest.RequestResult.NOT_DUPLICATE -> {
                        showToast("사용 가능한 아이디 입니다.")
                        println("사용 가능한 아이디 ")
			
			// 중복확인 결과를 저장한다.
                        id_check = true
			
			// 중복확인 버튼의 TEXT를 변경한다.
                        (it as Button).text = "사용가능"
                    }
		    // 중복된 id인 경우
                    HttpRequest.RequestResult.DUPLICATE -> {
                        showToast("중복된 아이디 입니다.")
                        println("중복된 아이디 ")
			
			// 중복확인 결과를 저장한다.
                        id_check = false
			
			// 중복확인 버튼의 TEXT를 변경한다.
                        (it as Button).text = "중복확인"
                    }

                    HttpRequest.RequestResult.SERVER_ERROR -> {
                        showToast("서버에 문제가 발생하였습니다.")
                    }

                    else -> {
                    }
                }
            }
            // 입력한 id가 올바른 형식으로 작성되지 않은 경우
            else
                showToast("아이디의 길이는 4 이상 10이하 입니다.")
        }
```

#### HttpRequest checkIdExist()
ID 중복 확인 요청을 하는 Connection Thread를 생성하고 실행하는 함수  


```kotlin 
  // ID 중복 확인 후 결과 RequestResult 반환
    // RequestResult.DUPLICATE  ID 중복
    // RequestResult.NOT_DUPLICATE  ID 사용가능
    fun checkIdExist(action: Action = Action.CHECK_DUPLICATION , id:String): RequestResult{
        // 요청 Thread 생성

        return try {
            // 요청 시작
            connectThread.start()
            // 요청 결과 대기 타임아웃 3초
            connectThread.join(3000)
            // 요청 결과 반환
            connectThread.getResult()
        } catch (e: Exception) {
            // 에러 발생시 리턴
            RequestResult.SERVER_ERROR;
        }

    }
```
### ConnectThread
요청에 필요한 설정을 하고 URI를 생성하고 실제로 요청을 보내 결과를 받는 Thread  
중복된 id인 경우 "ID_EXIST" 문자열이, 사용가능한 id인경우 "ID_NOT_EXIST" 문자열이  responseStr에 저장된다.  


```kotlin
 class ConnectThread(var action: Action) : Thread() {

        private var result = HttpRequest.RequestResult.SERVER_ERROR
        lateinit var conn: HttpURLConnection
	// 중복 확인 하려는 ID
        private var id = ""
	
	// 요청의 결과로 받은 문자열을 저장할 변수
   	private var responseStr = ""
	
        constructor(action: Action, id: String) : this(action) {
            this.id = id
        }
	
	// 요청의 결과를 반환하는 함수
        fun getResult(): RequestResult = result
	
	// 요청의 결과로 문자열을 받는경우 결과 문자열을 반환하는 함수
        fun getResultStr(): String = responseStr
	
 	override fun run() {
            try {
                var message: String = ""

                // 요청시 전달할 Param 목록
                val params:MutableMap<String, Any> = mutableMapOf()
		
                // 요청 종류에 따라 URI, HttpMethod ,Parameter 설정
                when (action) {
                    Action.CHECK_DUPLICATION -> {
                        serverUrl = "$SERVER_URI/user/idExist?id=${id}"
                        methodType="GET"
                    }
		    
                 	/* 중략 */
			
                }

                val url: URL = URL(serverUrl)
                //요청과 관련된 설정
                conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = methodType
                    setRequestProperty("Accept-Charset", "UTF-8") // Accept-Charset 설정.
                    setRequestProperty(
                        "Content-Type",  "application/json"
                    )
                    connectTimeout = 10000;
                    doOutput = true
                }
	
                     /* 중략 */
		     
                // 요청 성공시 처리할 로직
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {

                        Log.d(TAG, "요청 성공")
                        val br = BufferedReader(InputStreamReader(conn.inputStream,"UTF-8"))

                        //응답으로 받은 데이터 처리
                        var sb:StringBuilder = StringBuilder()
                        while (true) {
                            val str: String = br.readLine() ?: break
                            sb.append(str)
                        }
                        br.close()
                        responseStr = sb.toString()
                        println(responseStr)

                            result = when (action) {
                                Action.CHECK_DUPLICATION -> {
                                    when (responseStr) {
                                        "ID_NOT_EXIST" -> RequestResult.NOT_DUPLICATE
                                        "ID_EXIST" -> RequestResult.DUPLICATE
                                        else -> RequestResult.FAILED
                                    }
                                }
                                else -> RequestResult.SUCCESS
                            }
		
                } 
		//요청 실패시 처리.
		else {
                    result = HttpRequest.RequestResult.FAILED
                    Log.d(TAG, "실패")
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "SERVER 연결 실패")
                return
            }
	   finally {
                if(conn != null)
                  conn.disconnect()
            }
        }
```

### User Controller  
ID 중복 요청을 처리하는 User Controller  

```java
	// 아이디 중복확인 요청 처리
	@RequestMapping("idExist")
	@ResponseBody
	public String idExist(@RequestParam String id) {		
		return userService.isIdExist(id) ?  "ID_EXIST" : "ID_NOT_EXIST";	
	}
```


### 회원 가입 버튼  
회원 가입버튼을 활성화 하기 위해서는 모든 입력칸을 채워야하며, ID 중복확인을 필수적으로 진행 해야만한다.   
ID 중복확인시 중복확인 버튼의 TEXT가 (중복확인 -> 사용가능) 변경된다.
회원 가입 버튼이 활성화되고 버튼을 누르면 회원가입 요청을 시작하고 성공시 로그인화면으로 이동한다.


![캡dasd처](https://user-images.githubusercontent.com/81062639/141126987-0426fab1-36c4-4afe-b383-593b8b304f78.PNG)

### 회원가입 요청
사용자가 입력한 정보를 User DataBase에 등록하는 요청을 보낸다.  

#### 회원 가입 버튼 클릭 
회원 가입 버튼을 누르면 HttpRequest의 registerAccount()를 호출하여 서버에 요청을 보낸다.       
createUserData()는 사용자가 입력한 정보로 UserData 객체를 생성하여 반환하는 함수이다.  
요청을 받은 서버는 User정보를 DataBase에 등록하고 결과를 반환한다.    
  
```kotlin
        //가입 버튼 클릭
        v.btn_register.setOnClickListener {
            // createUserData() 사용자가 입력한 정보로 UserData 객체를 생성하여 반환한다.
            // 계정 등록 요청을 보낸다.
            when (HttpRequest.registerAccount(userData = createUserData())) {

                HttpRequest.RequestResult.SUCCESS -> {
                    showToast("환영합니다!")
                    println("회원가입 성공")
                    
                    // 로그인 페이지로 이동한다.
                    navController.popBackStack()
                }
                HttpRequest.RequestResult.FAILED -> {
                    showToast("회원가입에 실패했습니다")
                    println("회원가입 실패")
                }
                HttpRequest.RequestResult.SERVER_ERROR -> {
                    showToast("서버에 문제가 발생하였습니다.")
                }
                else -> {}
            }
        }
```

#### HttpRequest registerAccount()
유저 계정 등록 요청을 하는 Connection Thread를 생성하고 실행하는 함수  

```kotlin
   fun registerAccount(action: Action = Action.REGISTER_ACCOUNT,  userData: UserData):RequestResult{
        // 요청 Thread 생성
        val connectThread = ConnectThread(action, userData)
        return try {
            // 요청 시작
            connectThread.start()
            // 요청 결과 대기 타임아웃 3초
            connectThread.join(3000)
            // 요청 결과 반환
            connectThread.getResult()
        } catch (e: Exception) {
            // 에러 발생시 리턴
            RequestResult.SERVER_ERROR;
        }
    }
```


### ConnectThread
전체적인 코드는 회원가입시 사용했던 ConnectThread와 동일하다.  
(필요시 상단의 회원가입 ConnectThread 코드 참조)    
변경되는 부분은 요청 Paremeter와 URI등의 설정 이다.

``` kotlin

  class ConnectThread(var action: Action) : Thread() {
  	        /*   중략   */		
        override fun run() {
            try {
                // 요청시 전달할 Param 목록
                val params:MutableMap<String, Any> = mutableMapOf()
                // 요청 종류에 따라 URI, HttpMethod ,Parameter 설정
                when (action) {
                    Action.REGISTER_ACCOUNT -> {
	    
		    // 등록할 유저정보
                        params.put("name",userData.name)
                        params.put("age",userData.age)
                        params.put("id",userData.id)
                        params.put("pwd",userData.pwd)
                        params.put("created","")
			
		    // 요청 URI
                        serverUrl = "$SERVER_URI/user/register"
			
		    // HttpMethod
                        methodType="POST"
                    }                 
		        /*   중략   */			
                }

                val url: URL = URL(serverUrl)
                //요청과 관련된 설정
                conn = (url.openConnection() as HttpURLConnection).apply {
		
                    /*  중략  */		    
                    setRequestProperty(
                        "Content-Type",  "application/json"
                    )                  
                }	
                     if(params.isNotEmpty()) {
                            val bw = BufferedWriter(OutputStreamWriter(conn.outputStream))
			    
			    // 전달할 파라미터들을 Json 형식으로 변환해주는 함수 mapToJson()
                            bw.write(mapToJson(params))
                            bw.flush()
                            bw.close()
                     }		     
                // 요청 성공시 처리할 로직
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                       /*  중략  */		       
		       //요청 성공시 
		       result = HttpRequest.RequestResult.SUCCESS	       
                } else {
                    result = HttpRequest.RequestResult.FAILED
                    Log.d(TAG, "실패")
                }
            }
            catch (e: Exception) {
               /*  중략  */
            }
            finally {
              /*  중략  */
            }
        }
```

### User Controller  
계정 등록 요청을 처리하는 User Controller  

```java
	// 사용자 등록 요청 처리
	@PostMapping("register")
	@ResponseBody
	public ResponseEntity<String> register(@RequestBody User user) {
		return userService.register(user)
			? new ResponseEntity<String>(HttpStatus.OK) 
			: new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);	
	}
```  


## 로그인 
사용자가 입력한 id와 password 정보가 맞는 정보인지 서버에 확인 요청을 보낸다.  
로그인 성공시 로그인한 사용자의 정보가 Json 형식으로 반환된다.
해당 Json 데이터를 Gson을 이용하여 UserData로 파싱한다.  
UserInfo class를 이용하여 Sharedfreferences를 생성 하고 데이터를 저장한다.


![로그인화면](https://user-images.githubusercontent.com/81062639/141233039-567912f0-35f9-4e3c-97da-7feafff39fd8.PNG)



### UserInfo
사용자의 로그인 정보를 유지하고 SharedPreferences를 이용하여 저장하고 관리하는 class이다.

```kotlin

class UserInfo(var context: Context) {
   // 데이터를 저장 할 파일 이름
    var fileName = "UserInfo"
	
    var  sharedPreference = context.getSharedPreferences(fileName,0)
   
   // 데이터를 저장하는 함수
    fun setString(key:String, value:String){
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }
     // 데이터를 조회하는 함수
    fun getString(key:String):String{
        return sharedPreference.getString(key,"")?:""
    }
}
```



### 로그인 버튼 
로그인 버튼을 누르면 HttpRequest의 getUserData()를 호출하여 서버에 로그인 정보 확인 요청을 보낸다.       

```kotlin
      //로그인 버튼 동작
        v.btn_login.setOnClickListener {
            // 입력한 id
            val id = v.editText_id.text.toString()
            // 입력한 pwd
            val pwd = v.editText_pwd.text.toString()

            if(id != "" && pwd != "") {

                val userData = UserData("", 0, id, pwd)
                
                // 서버에 로그인 확인 요청을 보내고 로그인 성공시 해당 유저의 정보(이름 나이 등) 정보를 받아온다.
                val json = HttpRequest.getUserData(HttpRequest.Action.LOGIN, userData)
		
		// 로그인 실패 
                if(json == ""){
                    println("로그인 실패")
                    showToast("로그인 실패")
                }
		
		// 로그인 성공 
                else{
                    println("로그인 성공")
                    showToast("로그인 성공")
                    
                    // 받아온 json Data를 UserData로 Parsing하고 SharedPreferences를 이용하여 저장한다.
                    var user = Gson().fromJson(json,UserData::class.java)
                    MainActivity.userInfo.setString("id", id.lowercase())
                    MainActivity.userInfo.setString("name",user.name.lowercase())
                    
                    //목록 조회 화면으로 이동
                    navController.navigate(R.id.action_login_Fragment_to_recyclerFragment)
                }

            }
            else
                showToast("로그인 정보를 입력해주세요")
        }
```

### HttpRequest getUserData()
유저 계정 등록 요청을 하는 Connection Thread를 생성하고 실행하는 함수  

```java
   // 로그인 요청을 보내고 성공시 해당 유저 데이터 반환
    fun getUserData(action:Action, user:UserData = UserData("",0,"","")): String{
        val connectThread = ConnectThread(action, user)
        connectThread.start()
        connectThread.join(5000)
        return URLDecoder.decode(connectThread.getResultStr(),"utf-8")
    }
```
### ConnectThread
전체적인 코드는 필요시 상단의 회원가입 ConnectThread 코드 참조  
변경되는 부분(Paremeter와 URI등의 설정)     
요청 성공시 : HttpRequest.RequestResult.SUCCESS    
요청 실패시 : HttpRequest.RequestResult.FAILED    

```kotlin
 class ConnectThread(var action: Action) : Thread() {
	
	/*  중략  */
	
        override fun run() {
            try {
                var message: String = ""
                // 요청시 전달할 Param 목록
                val params:MutableMap<String, Any> = mutableMapOf()

                when (action) {
                      Action.LOGIN -> {
		      // 확인 할 로그인 
                        params.put("id",userData.id )
                        params.put("pwd",userData.pwd)
                        methodType="POST"
		      //요청 URI
                        serverUrl = "$SERVER_URI/user/login"
                    }           
                }
		
	/*  중략  */
	
        }
```

### User Controller  
로그인 정보를 확인하는 요청을 처리하는 User Controller  

```java
	// 로그인 정보확인 Service
	@PostMapping("login")
	@ResponseBody
	public ResponseEntity<User> login(@RequestBody User user) {	
		//로그인 성공시 사용자 정보 리턴
		return userService.login(user) 
			// 로그인 성공시 User(사용자의 정보 class)를 반환 한다.
			? new ResponseEntity<User>(userService.get(user.getId()), HttpStatus.OK) 
			: new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
```

## 메모 조회
로그인을 성공적으로 완료하면 작성된 메모를 조회 할 수 있는 화면으로 이동한다.  
조회한 메모를 조회하는 화면과 우측하단의 새로운 메모 작성 버튼, 우측 상단의 새로고침 버튼으로 구성 되어있다.


![dasd](https://user-images.githubusercontent.com/81062639/141232817-37106358-eaaa-4c72-bd78-ff6345a19e39.PNG)


### 메모 목록 조회
메모들의 목록을 조회하는 요청을 보내고 요청의 결과로 Json 데이터를 받아 처리하여 RecyclerView에 보여준다.  
주의( HttpRequest.getNotices() 와 getNotices() 는 이름이 같지만 다른 클래스의 함수이다.)
 
 
```kotlin

    private fun getNotices(): ArrayList<NoticeItem> {
	// 메모 정보를 담을 List
        var itemlist: ArrayList<NoticeItem> = ArrayList()

        // 요청의 결과로 Json 메모들의 데이터를 받아온다.
        val json = HttpRequest.getNotices()
        println(json)

        // 데이터가 존재하는지 확인한다.
        if(json != "") {
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                // Json 데이터를 메모데이터를 관리하는 NoticeItem 으로 Parsing 한다.
                var jsonObject = jsonArray.getJSONObject(i)
                itemlist.add(
                    NoticeItem(
                        jsonObject.getInt("id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("author"),
                        jsonObject.getString("body"),
                        jsonObject.getString("date")
                    )
                )
            }
        }
        // 메모들의 정보 리스트를 반환한다.
        return itemlist
    }   
    
    // 메모 조회 요청과 결과를 처리하는 함수를 호출한다. (상단의 getNotices() 함수)
    val itemList = getNotices()
 
    if (itemList.isNotEmpty()){
        // 반환된 메모 List 가 비어있지 않다면 RecyclerAdapter에 해당 List를 등록한다.
	// adapter : RecylerAdapter 이다.
       adapter.setItemList(getNotices())
       }

    
```

### HttpRequest getNotices()
메모 목록 조회 요청을 작성, 수행하고 결과로 Json 데이터를 반환하는 함수

```kotlin
   // 메모 목록 조회 요청을 보내고 메모들의 데이터를 Json 형식으로 받아온다.
    fun getNotices(action:Action = Action.READ_NOTICE): String{
        val connectThread = ConnectThread(action)
        connectThread.start()
	
        //타임 아웃
        connectThread.join(5000)
	
	// 메모들의 데이터 Json을 반환한다.
        return URLDecoder.decode(connectThread.getResultStr(),"utf-8")
    }
```

### ConnectThread
전체적인 코드는 필요시 상단의 회원가입 ConnectThread 코드 참조  
변경되는 부분(Paremeter와 URI등의 설정)
```kotlin
 class ConnectThread(var action: Action) : Thread() {
	
	/*  중략  */
	
        override fun run() {
            try {
                var message: String = ""
                // 요청시 전달할 Param 목록
                val params:MutableMap<String, Any> = mutableMapOf()

                when(action) {
                    Action.READ_NOTICE -> {
                    methodType="GET"
                    serverUrl = "$SERVER_URI/notice/getNotices"
                    }       
                }
		
	/*  중략  */
		
	   // 요청 성공 시	
	   if (conn.responseCode == HttpURLConnection.HTTP_OK) {

                    Log.d(TAG, "요청 성공")
                    val br = BufferedReader(InputStreamReader(conn.inputStream,"UTF-8"))

                    //응답으로 받은 데이터 처리
                    var sb:StringBuilder = StringBuilder()
                    while (true) {
                        val str: String = br.readLine() ?: break
                        sb.append(str)
                    }
                    br.close()
                    responseStr = sb.toString()	
		    
	/*  중략  */
        }
```




### 새로고침 버튼
새로고침 버튼을 누르면 데이터베이스에 메모 목록 조회요청을 다시 보내 최신의 정보를 받아 화면을 업데이트한다.

```kotlin
//  상단의 툴바의 클릭 이벤트를 등록한다.
        v.toolbar.setOnMenuItemClickListener {
	// 툴바의 항목이 클릭됬을때 어떤 요소가 클릭되었는지에 따라 처리를 달리한다.
            when (it.itemId) {
                //새로고침 Menu를 클릭했을때 이벤트
                R.id.menu1 -> {
                    showToast("새로고침")
		    // getNotices()를 다시 호출하여 최신의 정보를 받아와 처리해 생성한 List를 adapter에 등록한다.
                    adapter.setItemList(getNotices())
		    
		    // adapter에게 List의 요소가 변경되었다는 사실을 알린다.
                    adapter.notifyDataSetChanged()
		    
                    true
                }
                else -> false
            }
        }
```

### 메모 추가 버튼
현재 화면에 보이는 메모의 갯수를 확인하고 최대 갯수를 초과하지 않는경우 메모를 추가하는 화면으로 이동하고 최대 갯수 초과시 관련 Toast메세지를 띄운다.

```kotlin
	// 메모 추가 버튼 클릭 이벤트 등록
        v.btn_floating.setOnClickListener {
	    //최대 갯수 검사
            if (adapter.itemCount() < 20){
	       //  메모 추가 창으로 이동한다.
                navController.navigate(R.id.action_recyclerFragment_to_memoFragment)
	    }
	    
            else
                showToast("메모 최대갯수 제한")
        }
```
