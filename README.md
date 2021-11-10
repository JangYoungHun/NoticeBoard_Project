
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

### ConnectThread
전체적인 코드는 회원가입시 사용했던 ConnectThread와 동일하다. (필요시 상단의 회원가입 ConnectThread 코드 참조)  
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
