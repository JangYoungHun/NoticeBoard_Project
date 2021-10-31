package com.example.noticeboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.notice_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var itemList: ArrayList<NoticeItem> = ArrayList()
    val simpleDateFormat:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    lateinit var clickListener: RecyclerViewOnClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notice_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setText(itemList[position])
            // 각 아이템을 클릭 이벤트
            holder.itemView.setOnClickListener{
                clickListener.onClick(itemList,position)
            }



    }
    fun addClickListener(clickListener: RecyclerViewOnClick){
        this.clickListener = clickListener
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }


    fun setItemList(itemList:ArrayList<NoticeItem>){
        this.itemList.clear()
        this.itemList = itemList

    }

    fun addItem(item: NoticeItem) : NoticeItem {
        itemList.add(item)
        notifyItemInserted(itemList.size)
        return item
    }


    fun addItem(id:Int,title:String, author:String,body:String) : NoticeItem{
        val item =NoticeItem(id,title,author,body,simpleDateFormat.format(Date()))
        itemList.add(item)
        return item
    }

    fun addItem(title:String, author:String ) {
        itemList.add(NoticeItem(-1,title,author,"",simpleDateFormat.format(Date())))
        notifyItemInserted(itemList.size)
    }

    fun itemCount():Int = itemList.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvTitle: TextView = itemView.tv_title
        private var tvAuthor: TextView = itemView.tv_author
        private var tvDate: TextView = itemView.tv_date

        fun setText(item: NoticeItem) {
            tvTitle.text = item.title
            tvAuthor.text = item.author
            tvDate.text = item.date
        }

    }
}