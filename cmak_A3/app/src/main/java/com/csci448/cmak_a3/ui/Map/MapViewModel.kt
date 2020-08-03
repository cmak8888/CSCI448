package com.csci448.cmak_a3.ui.Map

import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.csci448.cmak_a3.data.Location as LocationClass
import com.csci448.cmak_a3.data.LocationRepository
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

private const val logTag = "448.MapViewModel"

class MapViewModel(private val locationRepository: LocationRepository)  : ViewModel() {
    private lateinit var lastLoc : LocationClass


    val locationLiveData = locationRepository.getLocations()


//    fun getLocationByAddress(address: String) : String{
//        val location = locationRepository.getLocationByAddress(address)
//        //return "${location.Address}\nTemp: ${location.Temperature}\nWeather: ${location.Weather}"
//        return "test"
//    }

    fun addLocation(lastLocation: Location, address: String, weather: String, temp: Double) {

//        val url = "https://api.openweathermap.org/data/2.5/onecall?lat=${lastLocation.latitude}&lon=${lastLocation.longitude}&appid=$API_KEY";
        val newLocation = LocationClass(lastLocation.latitude, lastLocation.longitude, temp, weather, address)
        locationRepository.addLocation(newLocation)
        lastLoc = newLocation
    }

    fun findLocation(location: Location) : String{

        return ""
    }



    fun deleteLocation(location: LocationClass) = locationRepository.deleteLocation(location)

    fun clear() {locationRepository.clear()}

}
