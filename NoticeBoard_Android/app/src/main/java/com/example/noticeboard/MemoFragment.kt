package com.example.noticeboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_memo.view.*

class MemoFragment : Fragment() {

    lateinit var v: View
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = requireActivity().findNavController(R.id.nav_host_fragment)


        v = inflater.inflate(R.layout.fragment_memo, container, false)

        v.btn_cancle.setOnClickListener {
            navController.popBackStack()
        }

        //메모 저장
        v.btn_ok.setOnClickListener {

            val title = v.editText_title.text.toString()
            val body = v.editText_body.text.toString()

            when{
                title=="" ->{
                    showToast("제목을 입력해주세요.")
                }
                body=="" ->{
                    showToast("내용을 입력해주세요.")
                }
                else ->{

                    val item = NoticeItem(0,title, MainActivity.userInfo.getString("id"),body, "")

                        // 웹서버에 memo 추가 요청 보내기
                        when (HttpRequest.connect(HttpRequest.Action.ADD_NOTICE, item)) {

                            HttpRequest.RequestResult.SUCCESS -> {
                                showToast("메모 추가 성공")
                            }
                            HttpRequest.RequestResult.FAILED -> {
                                showToast("메모 추가 실패")
                            }
                            HttpRequest.RequestResult.SERVER_ERROR -> {
                                showToast("서버에 문제가 발생하였습니다.")
                            }
                        }
                    navController.popBackStack()
                }
                }
            }


        return v
    }



    fun showToast(message: String) {
        Toast.makeText(activity?.applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
