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


        //화원가입 버튼 동작

        v.btn_join.setOnClickListener {
            navController.navigate(R.id.action_login_Fragment_to_createAccount_Fragment)
        }

        //로그인 버튼 동작
        v.btn_login.setOnClickListener {
            val id = v.editText_id.text.toString()
            val pwd = v.editText_pwd.text.toString()

            if(id != "" && pwd != "") {
                val userData = UserData("", 0, id, pwd)

                val json = HttpRequest.getJsonData(HttpRequest.Action.LOGIN, userData)

                if(json == ""){
                    println("로그인 실패")
                    showToast("로그인 실패")
                }

                else{
                    println("로그인 성공")
                    showToast("로그인 성공")

                    var user = Gson().fromJson(json,UserData::class.java)
                    MainActivity.userInfo.setString("id", id.lowercase())
                    MainActivity.userInfo.setString("name",user.name.lowercase())

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