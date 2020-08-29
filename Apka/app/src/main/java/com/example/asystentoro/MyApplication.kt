package com.example.asystentoro

import android.app.Application
import android.widget.ImageView
import android.widget.TextView
import com.apollographql.apollo.ApolloClient


public class MyApplication(): Application()
{
    companion object {
        var glat: Double? = null
        var glon: Double? = null
        var gcity: String? = null

        var weather: String? = null
        var city: String? = null
        var temp: Double? = null
        var press: Double? = null
        var hum: Double? = null

        var globalTask:ArrayList<DoTAsk>? = null

    }

}
