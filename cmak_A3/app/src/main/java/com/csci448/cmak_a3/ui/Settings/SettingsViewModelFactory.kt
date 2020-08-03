package com.csci448.cmak_a3.ui.Settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csci448.cmak_a3.data.LocationRepository

class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LocationRepository::class.java)
            .newInstance(LocationRepository.getInstance(context))
    }

}