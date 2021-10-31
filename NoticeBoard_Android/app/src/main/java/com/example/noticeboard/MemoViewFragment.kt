package com.example.noticeboard

import android.os.Bundle
import android.text.Html
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_memo_view.view.*


class MemoViewFragment : Fragment() {

    lateinit var navController:NavController
    lateinit var v:View
    var isAuthor = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navController = requireActivity().findNavController(R.id.nav_host_fragment)
        v =inflater.inflate(R.layout.fragment_memo_view, container, false)

        isAuthor = MainActivity.userInfo.getString("id") == arguments?.getString("author")?.lowercase()


        setTexts(arguments?.getString("title")!!,arguments?.getString("body")!!, isAuthor)

        //확인 버튼
        //수정시 update 필요
        v.btn_Ok.setOnClickListener {
            // 수정된 곳이 있다면
            if((v.et_title.text.toString() != arguments?.getString("title")!!)
                || v.et_body.text.toString() != arguments?.getString("body")!!) {

                val item = NoticeItem(
                    arguments?.getInt("id")!!,
                    v.et_title.text.toString(),
                    arguments?.getString("author")!!,
                    v.et_body.text.toString(),
                    ""
                )
                HttpRequest.connect(HttpRequest.Action.UPDATE_NOTICE, item)
            }
            navController.popBackStack()
        }

        // 삭제 버튼
        v.btn_Remove.setOnClickListener {
            // 삭제 요청
            if(isAuthor) {
                HttpRequest.delete(arguments?.getInt("id")!!)
                navController.popBackStack()
            }
        }

        return  v


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.userInfo.getString("name")
    }

    private fun setTexts(title:String, body:String, editable:Boolean){

        if(!editable){
            v.et_title.inputType = InputType.TYPE_NULL
            v.et_body.inputType = InputType.TYPE_NULL
        }

        v.et_title.setText("${title}")
        v.et_body.setText("${body}")
    }

}