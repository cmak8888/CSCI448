package com.csci448.cmak_a3.ui.Settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci448.cmak_a3.data.Location
import com.csci448.cmak_a3.data.LocationRepository
import java.util.*

class SettingsViewModel(private val locationRepository: LocationRepository) : ViewModel() {


    fun clear() {locationRepository.clear()}
}