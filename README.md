
# NoticeBoard_Project
Notice Board App &amp; Spring Server


# APP
![포트폴리오-004](https://user-images.githubusercontent.com/81062639/140050235-d32e4334-5595-414e-8c69-69e0a3ae9bcc.png)
![005](https://user-images.githubusercontent.com/81062639/140045936-a6d0da49-7b50-419a-9fe9-effae8468c1c.png)
![006](https://user-images.githubusercontent.com/81062639/140045968-229fd883-6c8f-42f8-b2bf-73c226826913.png)
![007](https://user-images.githubusercontent.com/81062639/140045978-5f7421db-8d28-454d-a19a-1f32f9159143.png)
![008](https://user-images.githubusercontent.com/81062639/140045982-92c4a182-79fd-4ca7-b92b-df62f3439943.png)


## 웹서버 요청 ACTION
웹서버 요청을 하는 object HttpRequest, 매개변수로 Action을 받아 해당하는 로직을 수행하는 Thread 실행

요청 타입에 따라 Thread를 생성하는 다양한 함수.
```java

    fun connect(action: Action, userData: UserData): ConnectResult {
        // Thread 생성
        val connectThread = ConnectThread(action, userData)
        try {
        //Thread 실행
            connectThread.start()
            //connectThread.join(3000)
            
            //Thread 실행 결과 반환
            return connectThread.getResult()
        } catch (e: Exception) {
            return ConnectResult.SERVER_ERROR
        }
    }
     fun connect(action: Action, noticeItem: NoticeItem): ConnectResult{/**/}
     fun delete( noticeId: Int, action: Action = Action.REMOVE_NOTICE){/**/}
```
```java
override fun run() {

// 요청시 전달할 Param 목록
val params:MutableMap<String, Any> = mutableMapOf()

// 요청 종류에 따라 URI, HttpMethod ,Parameter 설정
when (action){/*...*/}
                
val url: URL = URL(serverUrl)

//요청과 관련된 설정과 Connection 생성
conn = (url.openConnection() as HttpURLConnection).apply {
/* requestMethod, setRequestProperty 등 설정 */
}

//요청 성공시 수행할 로직
 if (conn.responseCode == HttpURLConnection.HTTP_OK){/*...*/}
 //요청 실패시 수행할 로직
 else{/*...*/}
 
}
```

## 요청 ACTION 종류
### HttpRequest.ACTION
![Action table](https://user-images.githubusercontent.com/81062639/140049557-59c7faf0-3dcd-4f04-9395-cc8448eed8c3.png)

# SERVER
## Notice_Server (구 버전)
#### spring을 사용하지 않고 자바 코드, 수동 DI를 사용.
한개의 서블릿이 요청을 받아 요청 타입에 따라 해당하는 함수를 호출하고 그결과를 반한한다.

5가지 버전의 프로젝트로 구성되어있다.
각각의 버전 수정관련 내용은 NoticeBoard_Server 수정 과정.txt 확인.

# Notice_Spring
#### Spring FrameWork를 사용하여 기존의 서버를 Rest 방식으로 재구성한 서버.

## Oracle DataBase 구조
![oracle](https://user-images.githubusercontent.com/81062639/140054802-8c3c1ace-e637-4279-b589-64e2de7d189d.PNG)



## REST URI
![rest uri](https://user-images.githubusercontent.com/81062639/140053856-b3741b07-4215-469e-9bb5-67401c735615.PNG)

