package com.example.noticeboard

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_create_account.view.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.editText_id
import kotlinx.android.synthetic.main.fragment_login.view.editText_pwd


class LoginFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        navController = requireActivity().findNavController(R.id.nav_host_fragment)
        val v = (inflater.inflate(R.layout.fragment_login, container, false))


        v.btn_join.setOnClickListener {
            navController.navigate(R.id.action_login_Fragment_to_createAccount_Fragment)
        }

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

                if(json == ""){
                    println("로그인 실패")
                    showToast("로그인 실패")
                }

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

        return v
    }

    fun showToast(message:String){
        Toast.makeText(activity?.applicationContext,message, Toast.LENGTH_SHORT).show()
    }


}