package com.example.asystentoro

import android.util.Log
import kotlin.collections.ArrayList

class DoTAsk() {
    var id:String? = null
    var title:String= ""
    var number:Int = -1
    var year: String? = null
    var month: String? = null
    var day:String? = null
    var hour: String? = null
    var minute: String? = null
    var date: String = ""
    var type: String? = null
    var text: String? = null

//    constructor(title: String,id: Int, dayofweek:String,hour:Int,minute:Int,type:String) : this(title,id) {
//        this.year = year
//        this.month=month
//        this.dayofweek=dayofweek
//        this.hour = hour
//        this.minute=minute
//        this.type=type
//    }

    fun dataConverter(tasks: DoTAsk): DoTAsk
    {

        val format = tasks.date.split("-","T",":").map { it.trim() }
        tasks.year = format[0]
        tasks.month = format[1]
        tasks.day = format[2]
        tasks.hour = format[3]
        tasks.minute = format[4]
        Log.d("Dobra:",format[0])
        return tasks
    }


    fun generateTaskList(tasks: ArrayList<DoTAsk>): ArrayList<ItemCardView> {
        val list = ArrayList<ItemCardView>()
        for (i in 0 until tasks.size) {
            val drawable = when (tasks[i].title.toLowerCase()) {
                "lekarz" -> R.drawable.cloud
                "baba" -> R.drawable.d01d
                "gg" -> R.drawable.d04d
                else -> R.drawable.common_google_signin_btn_icon_dark
            }
            val item = ItemCardView(drawable, tasks[i].title, "Data: ${tasks[i].day}-${tasks[i].month}-${tasks[i].year}   Time: ${tasks[i].hour}:${tasks[i].minute} ")
            list += item

        }
        return list
    }





}