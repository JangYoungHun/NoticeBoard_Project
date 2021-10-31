package com.example.noticeboard

import android.view.View

interface RecyclerViewOnClick {
    fun onClick(itemList:ArrayList<NoticeItem>, position:Int )
}