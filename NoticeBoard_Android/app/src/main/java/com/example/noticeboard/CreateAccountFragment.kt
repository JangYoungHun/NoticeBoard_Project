package com.example.noticeboard


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_create_account.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateAccountFragment : Fragment() {

    lateinit var v:View
    lateinit var navController: NavController
    var id_check:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navController = requireActivity().findNavController(R.id.nav_host_fragment)
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_create_account, container, false)

        //아이디 중복확인 버튼
        v.btn_idCheck.setOnClickListener {

            // 현재 입력한 id (중복확인할 id)
            val idInput =v.editText_id.text.toString()

            // 입력한 id가 올바른 형식으로 작성되었는지 확인.
            if(idValidity(idInput)) {
                //id 중복확인 요청
                when (HttpRequest.checkIdExist(id = idInput)) {
                    // 사용가능한 id일 경우
                    HttpRequest.RequestResult.NOT_DUPLICATE -> {
                        showToast("사용 가능한 아이디 입니다.")
                        println("사용 가능한 아이디 ")
                        id_check = true
                        (it as Button).text = "사용가능"
                    }
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



        v.editText_id.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                id_check = false
                v.btn_idCheck.text = "중복 확인"
            }
        })
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Check Information Validity
        GlobalScope.launch() {
            var preState = false

            while(true){
                delay(300L)
                var curState = infomValidity()
                if(preState != curState) {
                    val handler = Handler(Looper.getMainLooper())
                    handler.post {
                        v.btn_register.isEnabled = infomValidity()
                    }
                    preState = curState
                }
            }
        }
    }

    //id 조건 확인
    private fun idValidity(id:String) : Boolean{
        //id 길이 제한
        // 4 ~ 10
        return (id.length in 4..10)
    }

    //정보를 전부 입력 했는지 확인
    fun infomValidity() : Boolean {
        //각 정보별 문자형식 지정 필요
        //ex 특수문자 길이제한 등
        //password 시 & 있을경우 url에 문제 생길수 있다.
        val name = v.editText_name.text.toString()
        val age = v.editText_age.text.toString()
        val id = v.editText_id.text.toString()
        val pwd = v.editText_pwd.text.toString()

        return ( name != "" && age!="" && id!="" && pwd!="" && id_check)
    }


    fun showToast(message:String){
        Toast.makeText(activity?.applicationContext,message, Toast.LENGTH_SHORT).show()
    }

    fun createUserData():UserData{

        var age = 0
        if(v.editText_age.text.toString()!= ""){
            age = v.editText_age.text.toString().toInt()
        }

       return UserData(
            v.editText_name.text.toString(),
            age,
            v.editText_id.text.toString(),
            v.editText_pwd.text.toString()
        )
    }

}