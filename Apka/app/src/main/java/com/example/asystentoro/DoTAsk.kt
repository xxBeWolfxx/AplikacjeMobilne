package com.example.asystentoro

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DoTAsk() {
    var id: String? = null
    var title: String = ""
    var number: Int = -1
    var year: String? = null
    var month: String? = null
    var day: String? = null
    var hour: String? = null
    var minute: String? = null
    var date: String = ""
    var type: String? = null
    var text: String? = null
    var sortDate: Long = 0

//    constructor(title: String,id: Int, dayofweek:String,hour:Int,minute:Int,type:String) : this(title,id) {
//        this.year = year
//        this.month=month
//        this.dayofweek=dayofweek
//        this.hour = hour
//        this.minute=minute
//        this.type=type

//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun CurrentTask(tasks: ArrayList<DoTAsk>): Int? {

        val current = LocalDateTime.now()
        var soon: Int? = null
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
        var formatted = current.format(formatter).toString()

        var value = formatted!!.trim().toLong()

                for (i in 0 until tasks.size) {
                    if (value<tasks[i].sortDate){
                        soon = i
                        break
                    }

                }

                return soon

//        var listValues: ArrayList<Long> = ArrayList()
//        for (i in 0 until tasks.size) {
//
//            listValues.add(value!! - tasks[i].sortDate.toLong())
//        }
//        for (i in 0 until tasks.size) {
//
//        }
//        var po = listValues.indexOf(listValues.min())
//        val c: Int = 0
    }

    //// FORMAT: 20170802xxxx



    fun Sorting(list: ArrayList<DoTAsk>): ArrayList<DoTAsk> {



        for (i in 0  until list.size) {
        list[i].sortDate=list[i].year!!.toLong()*100000000+list[i].month!!.toLong()*1000000 +list[i].day!!.toLong()*10000+ list[i].hour!!.toLong()*100+list[i].minute!!.toLong()
        }
        var cos = list.sortedBy { it.sortDate }
        for (i in 0 until list.size)
            Log.d("UWAGA", cos[i].date)

        var new:ArrayList<DoTAsk> = ArrayList()
        new.addAll(cos)
        return new


    }


    fun dataConverter(tasks: DoTAsk): DoTAsk {
        if (tasks.date != "") {
            val format = tasks.date.split("-", "T", ":").map { it.trim() }
            tasks.year = format[0]
            tasks.month = format[1]
            tasks.day = format[2]
            tasks.hour = format[3]
            tasks.minute = format[4]
            Log.d("Dobra:", format[0])
        } else {
            tasks.date = "ARGH!"
        }
        return tasks
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
            if (tasks[i].date != "Urgent") {
                val item = ItemCardView(
                    drawable,
                    tasks[i].title,
                    "Data: ${tasks[i].day}-${tasks[i].month}-${tasks[i].year}   Time: ${tasks[i].hour}:${tasks[i].minute}",
                    tasks[i].number
                )
                list += item
            } else {
                val item = ItemCardView(drawable, tasks[i].title, "ARGH!", tasks[i].number)
                list += item
            }

        }
        return list
    }


}