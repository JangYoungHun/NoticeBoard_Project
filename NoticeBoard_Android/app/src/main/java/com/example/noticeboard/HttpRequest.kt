package com.example.noticeboard

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
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

    enum class ConnectResult {
        SERVER_ERROR,
        SUCCESS,
        FAILED,
        DUPLICATE,
        NOT_DUPLICATE
    }

    // private const val SERVER_URI: String = "http://172.30.1.40:8888/UserDAO"
    private const val SERVER_URI: String = "http://mrjangsserver.ddns.net:8888"
    private var serverUrl:String = ""
    private const val TAG: String = "HttpRequestLOG"
    var methodType:String = ""


    fun connect(action: Action, userData: UserData): ConnectResult {
        // 서버 켜져있는지 확인 코드 추가 필요
        val connectThread = ConnectThread(action, userData)
        try {
            connectThread.start()
            //차후 다른 방법있는지 공부후 변경
            // 최대 기다리는 시간 3초
            connectThread.join(3000)
            return connectThread.getResult()
        } catch (e: Exception) {
            return ConnectResult.SERVER_ERROR
        }
    }

    fun connect(action: Action, noticeItem: NoticeItem): ConnectResult {
        // 서버 켜져있는지 확인 코드 추가 필요
        val connectThread = ConnectThread(action, noticeItem)
        try {
            connectThread.start()
            //차후 다른 방법있는지 공부후 변경
            // 최대 기다리는 시간 3초
            connectThread.join(3000)
            return connectThread.getResult()
        } catch (e: Exception) {
            return ConnectResult.SERVER_ERROR
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

    fun getJsonData(action:Action, user:UserData = UserData("",0,"","")): String{
        val connectThread = ConnectThread(action, user)
        connectThread.start()
        connectThread.join(5000)
        return URLDecoder.decode(connectThread.getResultStr(),"utf-8")
    }



    class ConnectThread(var action: Action) : Thread() {


        lateinit var userData: UserData
        lateinit var noticeItem: NoticeItem
        private var result = HttpRequest.ConnectResult.SERVER_ERROR
        lateinit var conn: HttpURLConnection

        private var type = ""
        private var responseStr = ""
        private var noticeId = -1
        constructor(action: Action, userData: UserData) : this(action) {
            this.userData = userData
        }
        constructor(action: Action, noticeItem: NoticeItem) : this(action) {
            this.noticeItem = noticeItem
        }
        constructor(action: Action, noticeId:Int) : this(action) {
            this.noticeId = noticeId
        }

        fun getResult(): ConnectResult = result
        fun getResultStr(): String = responseStr

        override fun run() {
            try {
                val params:MutableMap<String, Any> = mutableMapOf()

                var message: String = ""
                when (action) {
                    Action.CHECK_DUPLICATION -> {
                        serverUrl = "$SERVER_URI/user/idExist?id=${userData.id}"
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
                    Action.GET_USER_INFO-> {
                        methodType="POST"
                        serverUrl = "$SERVER_URI/user/getUserInfo"
                        params["name"] = userData.name
                        params["age"] = userData.age
                        params["id"] = userData.id
                        params["pwd"] = userData.pwd
                        params["created"] = ""
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
                conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = methodType
                    setRequestProperty("Accept-Charset", "UTF-8") // Accept-Charset 설정.
                    setRequestProperty(
                        "Content-Type",  "application/json"
                    )

                    connectTimeout = 10000;
                    doOutput = true
                }

                try {
                    if((methodType=="POST")||(methodType=="PUT")) {
                        val bw = BufferedWriter(OutputStreamWriter(conn.outputStream))
                        bw.write(mapToJson(params))
                        bw.flush()
                        bw.close()
                    }
                } catch (e: Exception) {
                    throw e
                }


                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    // 요청 성공
                    try {
                        Log.d(TAG, "요청 성공")
                        val br = BufferedReader(InputStreamReader(conn.inputStream,"UTF-8"))
                        var sb:StringBuilder = StringBuilder()

                        while (true) {
                            val str: String = br.readLine() ?: break
                            sb.append(str)
                        }
                        br.close()
                        responseStr = sb.toString()
                        println(responseStr)

                        if(action == Action.READ_NOTICE){
                            return
                        }
                        else if (action == Action.REGISTER_ACCOUNT){
                            result = HttpRequest.ConnectResult.SUCCESS
                            return
                        }

                        else {
                            result = when (action) {
                                Action.CHECK_DUPLICATION -> {
                                    when (responseStr) {
                                        "ID_NOT_EXIST" -> HttpRequest.ConnectResult.NOT_DUPLICATE
                                        "ID_EXIST" -> HttpRequest.ConnectResult.DUPLICATE
                                        else -> HttpRequest.ConnectResult.FAILED
                                    }
                                }
                                Action.LOGIN -> {
                                    when (responseStr) {
                                        "SUCCESS" -> HttpRequest.ConnectResult.SUCCESS
                                        "FAILED" -> HttpRequest.ConnectResult.FAILED
                                        else -> HttpRequest.ConnectResult.FAILED
                                    }
                                }
                                Action.ADD_NOTICE -> {
                                    when (responseStr) {
                                        "SUCCESS" -> HttpRequest.ConnectResult.SUCCESS
                                        "FAILED" -> HttpRequest.ConnectResult.FAILED
                                        else -> HttpRequest.ConnectResult.FAILED
                                    }
                                }
                                else -> HttpRequest.ConnectResult.FAILED

                            }
                        }
                    } catch (e: SocketTimeoutException) {
                        throw e
                    } catch (e: Exception) {
                        throw e
                    }


                } else {
                    result = HttpRequest.ConnectResult.FAILED
                    Log.d(TAG, "실패")

                }
                conn.disconnect()


            }catch (e: SocketTimeoutException) {
                e.printStackTrace()
                Log.d(TAG, "SERVER 연결 실패")
                return
            }
            catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "SERVER 연결 실패")
                return
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