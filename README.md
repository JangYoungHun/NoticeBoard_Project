# NoticeBoard_Project
Notice Board App &amp; Spring Server
![tjfaud](https://user-images.githubusercontent.com/81062639/140045708-f094a8a5-c9a3-4ed4-89a5-c47bb9afdd55.PNG)
![005](https://user-images.githubusercontent.com/81062639/140045936-a6d0da49-7b50-419a-9fe9-effae8468c1c.png)
![006](https://user-images.githubusercontent.com/81062639/140045968-229fd883-6c8f-42f8-b2bf-73c226826913.png)
![007](https://user-images.githubusercontent.com/81062639/140045978-5f7421db-8d28-454d-a19a-1f32f9159143.png)
![008](https://user-images.githubusercontent.com/81062639/140045982-92c4a182-79fd-4ca7-b92b-df62f3439943.png)

# APP
## 웹서버 요청 ACTION
웹서버 요청을 하는 object HttpRequest, 매개변수로 Action을 받아 해당하는 로직을 수행하는 Thread 실행
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

## 서버
### 1. Notice_Server
