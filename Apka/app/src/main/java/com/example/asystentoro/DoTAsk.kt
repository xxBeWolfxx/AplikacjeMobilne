package com.example.asystentoro

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        if (tasks.date != "") {
            val format = tasks.date.split("-", "T", ":").map { it.trim() }
            tasks.year = format[0]
            tasks.month = format[1]
            tasks.day = format[2]
            tasks.hour = format[3]
            tasks.minute = format[4]
            Log.d("Dobra:", format[0])
        }
        else {tasks.date = "ARGH!" }
        return tasks
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun LookingForTheEarliest()
    {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        val list = listOf("14-10-2016 | 15:48",
            "01-08-2015 | 09:29",
            "15-11-2016 | 19:43")
        val result = list.sortedByDescending {
            LocalDate.parse(it, dateTimeFormatter)
        }
    var kkkk = 0
    }

    fun generateTaskList(tasks: ArrayList<DoTAsk>): ArrayList<ItemCardView> {
        val list = ArrayList<ItemCardView>()
        for (i in 0 until tasks.size) {
            val drawable = when (tasks[i].type?.toLowerCase()) {
                "meeting" -> R.drawable.meeting
                "shop list" -> R.drawable.shoplist
                "to do" -> R.drawable.todo
                "other" -> R.drawable.qmark
                else -> R.drawable.circle
            }
            if (tasks[i].date!="Urgent")
            {
                val item = ItemCardView(drawable, tasks[i].title, "Data: ${tasks[i].day}-${tasks[i].month}-${tasks[i].year}   Time: ${tasks[i].hour}:${tasks[i].minute}", tasks[i].number)
                list += item
            }
            else{
                val item = ItemCardView(drawable, tasks[i].title, "ARGH!", tasks[i].number)
                list += item
            }

        }
        return list
    }





}