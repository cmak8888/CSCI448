package com.csci448.cmak_a3.ui.Map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csci448.cmak_a3.data.LocationRepository

class MapViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LocationRepository::class.java)
            .newInstance(LocationRepository.getInstance(context))
    }
}