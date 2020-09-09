package com.dimfcompany.akcslclient.base

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.dimfcompany.akcslclient.di.application.ComponentApplication
import com.dimfcompany.akcslclient.di.application.DaggerComponentApplication
import com.dimfcompany.akcslclient.logic.utils.DateManager

//Todo remove not needed loggers!!
class AppClass : Application()
{
    companion object
    {
        lateinit var app: AppClass
        var top_activity: AppCompatActivity? = null
        lateinit var app_component: ComponentApplication
        lateinit var gson: Gson
    }

    override fun onCreate()
    {
        super.onCreate()

        app = this
        app_component = DaggerComponentApplication.builder()
                .build()

        gson = GsonBuilder()
                .setDateFormat(DateManager.FORMAT_FOR_SERVER_LARAVEL)
                .create()

    }
}