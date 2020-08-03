package edu.mines.csci448.lab.criminalintent

import android.app.Application
import android.util.Log

class CriminalIntentApplication : Application() {
    private val LOG_TAG = "csci448.CriminalIntent"
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate called")
    }
}