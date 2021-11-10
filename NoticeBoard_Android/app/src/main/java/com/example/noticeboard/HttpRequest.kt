package com.example.noticeboard

import android.util.Log
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLDecoder
import org.json.JSONObject
import java.io.*


object HttpRequest {

    enum class Action {
        CHECK_DUPLICATION,
        REGISTER_ACCOUNT,
        LOGIN,
        ADD_NOTICE,
        READ_NOTICE,
        GET_USER_INFO,
        REMOVE_NOTICE,
        UPDATE_NOTICE
    }

    enum class RequestResult {
        SERVER_ERROR,
        SUCCESS,
        FAILED,
        DUPLICATE,
        NOT_DUPLICATE
    }


    private const val SERVER_URI: String = "http://172.30.1.40:8080"
    private var serverUrl:String = ""
    private const val TAG: String = "HttpRequestLOG"
    var methodType:String = ""


    fun connect(action: Action, userData: UserData): RequestResult {
        // 서버 켜져있는지 확인 코드 추가 필요
        val connectThread = ConnectThread(action, userData)
        try {
            connectThread.start()
            //차후 다른 방법있는지 공부후 변경
            // 최대 기다리는 시간 3초
            connectThread.join(3000)
            return connectThread.getResult()
        } catch (e: Exception) {
            return RequestResult.SERVER_ERROR
        }
    }

    fun connect(action: Action, noticeItem: NoticeItem): RequestResult {
        // 서버 켜져있는지 확인 코드 추가 필요
        val connectThread = ConnectThread(action, noticeItem)
        try {
            connectThread.start()
            //차후 다른 방법있는지 공부후 변경
            // 최대 기다리는 시간 3초
            connectThread.join(3000)
            return connectThread.getResult()
        } catch (e: Exception) {
            return RequestResult.SERVER_ERROR
        }
    }

    // ID 중복 확인 후 결과 RequestResult 반환
    // RequestResult.DUPLICATE  ID 중복
    // RequestResult.NOT_DUPLICATE  ID 사용가능
    fun checkIdExist(action: Action = Action.CHECK_DUPLICATION , id:String): RequestResult{
        // 요청 Thread 생성
        val connectThread = ConnectThread(action, id)
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

    fun delete( noticeId: Int, action: Action = Action.REMOVE_NOTICE) {
        // 서버 켜져있는지 확인 코드 추가 필요
        val connectThread = ConnectThread(action, noticeId)
        try {
            connectThread.start()
        } catch (e: Exception) {
        }
    }

    // 로그인 요청을 보내고 성공시 해당 유저 데이터 반환
    fun getUserData(action:Action, user:UserData = UserData("",0,"","")): String{
        val connectThread = ConnectThread(action, user)
        connectThread.start()
        connectThread.join(5000)
        return URLDecoder.decode(connectThread.getResultStr(),"utf-8")
    }



    class ConnectThread(var action: Action) : Thread() {


        private var userData: UserData = UserData("",0,"","")
        private var noticeItem: NoticeItem = NoticeItem(-1,"","","","")
        private var result = HttpRequest.RequestResult.SERVER_ERROR
        lateinit var conn: HttpURLConnection

        private var id = ""
        private var type = ""
        private var responseStr = ""
        private var noticeId = -1

        constructor(action: Action, userData: UserData) : this(action){
            this.userData = userData
        }

        constructor(action: Action, id: String) : this(action) {
            this.id = id
        }
        constructor(action: Action, noticeItem: NoticeItem) : this(action)
        constructor(action: Action, noticeId:Int) : this(action) {
            this.noticeId = noticeId
        }

        fun getResult(): RequestResult = result
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
                    Action.REGISTER_ACCOUNT -> {
                        params.put("name",userData.name)
                        params.put("age",userData.age)
                        params.put("id",userData.id)
                        params.put("pwd",userData.pwd)
                        params.put("created","")

                        serverUrl = "$SERVER_URI/user/register"
                        methodType="POST"
                    }
                    Action.LOGIN -> {
                        params.put("id",userData.id )
                        params.put("pwd",userData.pwd)
                        methodType="POST"
                        serverUrl = "$SERVER_URI/user/login"
                    }
                    Action.ADD_NOTICE -> {
                        params.put("title",noticeItem.title)
                        params.put("author",noticeItem.author)
                        params.put("body",noticeItem.body)
                        params.put("date","")
                        methodType="POST"
                        serverUrl = "$SERVER_URI/notice/addNotice"
                    }

                    Action.READ_NOTICE -> {
                        methodType="GET"
                        serverUrl = "$SERVER_URI/notice/getNotices"
                    }

                    Action.REMOVE_NOTICE ->{
                        methodType="DELETE"
                        serverUrl = "$SERVER_URI/notice/${noticeId}"
                    }

                    Action.UPDATE_NOTICE ->{
                        params.put("title",noticeItem.title)
                        params.put("author",noticeItem.author)
                        params.put("body",noticeItem.body)
                        params.put("date","")

                        methodType="PUT"
                        serverUrl = "$SERVER_URI/notice/${noticeItem.id}"

                    }
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

                     if(params.isNotEmpty()) {
                            val bw = BufferedWriter(OutputStreamWriter(conn.outputStream))
                            bw.write(mapToJson(params))
                            bw.flush()
                            bw.close()
                     }
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

                } else {
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

    }

    fun mapToJson(map: MutableMap<String, Any>): String? {
        val json = JSONObject()
        for ((key, value) in map) {
            json.put(key, value)
        }
        return json.toString()
    }

}