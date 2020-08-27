package com.example.asystentoro

class DoTAsk() {
    var id:String? = null
    var title:String= ""
    var year: Int? = null
    var month: Int? = null
    var dayofweek: String? = null
    var hour: Int? = null
    var date:String = ""
    var minute: Int? = null
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


    fun generateTaskList(tasks: ArrayList<DoTAsk>): List<ItemCardView> {
        val list = ArrayList<ItemCardView>()
        for (i in 0 until tasks.size) {
            val drawable = when (tasks[i].title.toLowerCase()) {
                "lekarz" -> R.drawable.cloud
                "baba" -> R.drawable.d01d
                "gg" -> R.drawable.d04d
                else -> R.drawable.common_google_signin_btn_icon_dark
            }
            val item = ItemCardView(drawable, tasks[i].title, tasks[i].date)
            list += item
        }
        return list
    }





}