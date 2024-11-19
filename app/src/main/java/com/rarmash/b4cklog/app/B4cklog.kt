package com.rarmash.b4cklog.app

import android.app.Application
import com.rarmash.b4cklog.network.RetrofitClient

class B4cklog : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.initialize(this)
    }
}