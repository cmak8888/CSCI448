package com.csci448.cmak_a2.ui.Preferences

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csci448.cmak_a2.data.GameRepository

// PreferencesViewModelFactory.kt
class PreferencesViewModelFactory(private val context: Context)
    : ViewModelProvider.Factory {
    override fun<T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GameRepository::class.java)
            .newInstance(GameRepository.getInstance(context))
    }
}

