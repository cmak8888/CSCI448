package com.csci448.cmak_a3.ui.History

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci448.cmak_a3.data.Location
import com.csci448.cmak_a3.data.LocationRepository
import java.util.*

class HistoryViewModel(private val locationRepository: LocationRepository) : ViewModel() {

    val locationLiveData = locationRepository.getLocations()

    var locations = locationRepository.getLocations()

    fun getLocation(id: UUID) {
        locationRepository.getLocation(id)
    }

    fun deleteLocation(location: Location) = locationRepository.deleteLocation(location)

    fun clear() {locationRepository.clear()}
}