package com.example.xpensemanager

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class XMApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
