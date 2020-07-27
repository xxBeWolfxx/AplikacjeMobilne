package com.example.asystentoro

class DoTAsk(var title:String, var id:Int) {
    var year: Int? = null
    var month: Int? = null
    var dayofweek: String? = null
    var hour: Int? = null
    var minute: Int? = null
    var type: String? = null
    var text: String? = null

    constructor(title: String,id: Int, dayofweek:String,hour:Int,minute:Int,type:String) : this(title,id) {
        this.year = year
        this.month=month
        this.dayofweek=dayofweek
        this.hour = hour
        this.minute=minute
        this.type=type
    }

    fun WriteDescription(text:String)
    {
        this.text=text
    }
    fun LookingForEarliest(tab: Array<DoTAsk>):Unit
    {
        var i:Int = tab.size
        var temp:Int = 0
        var time:Int = 0



    }



}