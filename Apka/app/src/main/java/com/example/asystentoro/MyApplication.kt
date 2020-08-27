package com.example.asystentoro

import android.app.Application

class MyApplication(): Application()
{
    private var GlobalTask = DoTAsk()

    fun getGlobalTask(): DoTAsk {
        return GlobalTask
    }

}
