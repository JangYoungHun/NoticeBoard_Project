package com.example.noticeboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_recycler.view.*
import kotlinx.android.synthetic.main.fragment_recycler.view.toolbar
import org.json.JSONArray


class RecyclerFragment : Fragment() {
    lateinit var v: View
    lateinit var adapter: RecyclerAdapter
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navController = requireActivity().findNavController(R.id.nav_host_fragment)

        v = inflater.inflate(R.layout.fragment_recycler, container, false)
        v.toolbar.inflateMenu(R.menu.menu_main)
        v.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                //새로고침 Menu
                R.id.menu1 -> {
                    showToast("새로고침")
                    adapter.setItemList(getNotices())
                    adapter.notifyDataSetChanged()
                    true
                }
                else -> false
            }
        }

        v.btn_floating.setOnClickListener {
            if (adapter.itemCount() < 20)
                navController.navigate(R.id.action_recyclerFragment_to_memoFragment)
            else
                showToast("메모 최대갯수 제한")
        }

        adapter = RecyclerAdapter()



        adapter.addClickListener(object : RecyclerViewOnClick {
            override fun onClick(itemList: ArrayList<NoticeItem>, position: Int) {
                var bundle = Bundle().apply {
                    putInt("id", itemList[position].id)
                    putString("date", itemList[position].date)
                    putString("author", itemList[position].author)
                    putString("title", itemList[position].title)
                    putString("body", itemList[position].body)
                }
                navController.navigate(R.id.action_recyclerFragment_to_memoViewFragment, bundle)
            }
        })


        val itemList = getNotices()
        if (itemList.isNotEmpty())
            adapter.setItemList(getNotices())

        v.recyclerView.adapter = adapter
        v.recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        v.recyclerView.setHasFixedSize(true)


        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            showToast("logout")
            MainActivity.userInfo.setString("id", "")
            navController.popBackStack()
        }


        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       //println("user name : ${MainActivity.userInfo.getString("name")}")
    }

    fun addItem(item: NoticeItem): NoticeItem {
        return adapter.addItem(item)
    }

    fun addItem(id:Int,title: String, author: String, body: String): NoticeItem {
        return adapter.addItem(id,title, author, body)
    }

    fun showToast(message: String) {
        Toast.makeText(activity?.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun getNotices(): ArrayList<NoticeItem> {
        var itemlist: ArrayList<NoticeItem> = ArrayList()
        val json = HttpRequest.getJsonData(HttpRequest.Action.READ_NOTICE)
        println(json)
        if(json != "") {
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {

                var jsonObject = jsonArray.getJSONObject(i)
                itemlist.add(
                    NoticeItem(
                        jsonObject.getInt("id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("author"),
                        jsonObject.getString("body"),
                        jsonObject.getString("date")
                    )
                )
            }
        }
        return itemlist
    }


}