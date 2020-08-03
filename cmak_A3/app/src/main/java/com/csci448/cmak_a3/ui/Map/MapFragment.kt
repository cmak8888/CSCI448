package com.csci448.cmak_a3.ui.Map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.csci448.cmak_a3.R
import com.csci448.cmak_a3.data.Location as LocationClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URL
import kotlin.text.StringBuilder

private val logTag = "448.Map"
private const val REQUEST_LOC_PERMISSION = 1
private const val API_KEY = "701b88d9bcd80ed81732df3cbab9bb68";
private lateinit var lastLocation: Location

//class Weather {
//    lateinit var weather: String
//}
//
//class Temperature {
//    var temp: Double = 0.00
//}

class MapFragment : SupportMapFragment() {

    private lateinit var locationRequest: LocationRequest
    private lateinit var lastLocationData: com.csci448.cmak_a3.data.Location
    private lateinit var mapViewModel: MapViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var googleMap: GoogleMap
    private lateinit var fab: FloatingActionButton
    private lateinit var mapFrag : CoordinatorLayout
    private var saveData = true
    private var delete = false
//    private var saving = false

    private var locationUpdateState = false

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(logTag, "onCreate")
        super.onCreate(savedInstanceState)

        val factory = MapViewModelFactory(requireContext())
        mapViewModel =
            ViewModelProvider(this, factory).get(MapViewModel::class.java)

        locationRequest = LocationRequest.create()
        locationRequest.interval = 0
        locationRequest.numUpdates = 1
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        getMapAsync { map ->
            googleMap = map
            if(delete) {
                googleMap.clear()
                val sharedPref: SharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context)
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putBoolean("delete", false)
                editor.apply()
                delete = false
            }
            googleMap.setOnMapLoadedCallback {
                googleMap.setOnMarkerClickListener{ marker ->
                    val loc = marker.tag as LocationClass
                    Snackbar.make(mapFrag, "${loc.time} \nTemp: ${loc.Temperature.toString()} Weather: ${loc.Weather}" , Snackbar.LENGTH_INDEFINITE).setAction("Delete") { view: View? ->
                        Log.d(logTag, "Deleting")
                        AlertDialog.Builder(requireContext())
                            .setTitle(R.string.confirm_delete)
                            .setMessage(requireContext().resources.getString(R.string.confirm_delete_message, loc.Address))
                            .setIcon(R.drawable.ic_delete)
                            .setPositiveButton(android.R.string.yes) { _, _ ->
                                mapViewModel.deleteLocation(loc)
                                Toast.makeText(requireContext(), "Attempting to delete if possible.", Toast.LENGTH_SHORT).show()
                                googleMap.clear()
                                drawMarkers()
                            }
                            .setNegativeButton(android.R.string.no) { dialog, _ -> dialog.cancel() }
                            .show()

                    }.show()
                    true
                }
            }
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapFrag = view.findViewById(R.id.maplayout)
        val mapView = super.onCreateView(inflater, container, savedInstanceState)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            checkPermissionAndGetLocation()

        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.d(logTag, "Got a location: ${locationResult.lastLocation}")
                lastLocation = locationResult.lastLocation
                Snackbar.make(view, getAddress(lastLocation), Snackbar.LENGTH_LONG).show()
                weatherTask().execute()
                Log.d(logTag, "Location added")
            }
        }


        mapFrag.addView(mapView)
        return mapFrag
    }

    private fun getAddress(location: Location): String {
        val geocoder = Geocoder(requireActivity())
        val addressTextBuilder = StringBuilder()
        try {
            val addresses = geocoder.getFromLocation(location.latitude,
                location.longitude,
                1)
            if(addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                for(i in 0..address.maxAddressLineIndex) {
                    if(i > 0) {
                        addressTextBuilder.append( "\n" )
                    }
                    addressTextBuilder.append( address.getAddressLine(i) )
                }
            }
        } catch (e: IOException) {
            Log.e(logTag, e.localizedMessage)
        }
        return addressTextBuilder.toString()
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context) ?: return
        saveData = sharedPref.getBoolean("saveData", true)
        delete = sharedPref.getBoolean("delete", false)

        Log.d(logTag, "on Start Shared = ${sharedPref.getBoolean("saveData", true)}")
        checkIfLocationCanBeRetrieved()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")

        drawMarkers()
    }

    private fun drawMarkers() {
        mapViewModel.locationLiveData.observe(
            viewLifecycleOwner,
            Observer { locations ->
                locations?.let {
                    Log.d(logTag, "Got locations ${locations.size}")
                    locations.forEach {
                            location: LocationClass -> Unit
                        updateUI(location)
                    }
                }
            }
        )
    }

    private fun updateUI(locationData: com.csci448.cmak_a3.data.Location) {
        // make sure we have a map and a location
        if( !::googleMap.isInitialized || !::lastLocation.isInitialized ) {
            return
        }
        // create a point for the corresponding lat/long
        val myLocationPoint = LatLng(locationData.Latitude,
            locationData.Longitude)
        // Step 3 will go here
        // include all points that should be within the bounds of the zoom
        // convex hull
        val bounds = LatLngBounds.Builder()
            .include(myLocationPoint)
            .build()
        // add a margin
        val margin = resources.getDimensionPixelSize(R.dimen.map_inset_margin)
        // create a camera to smoothly move the map view
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, margin)
        // move our camera!
        googleMap.animateCamera(cameraUpdate)
        // create the marker


        val myMarker = MarkerOptions()
            .position(myLocationPoint)
            .title(locationData.Address)
        // clear any prior markers on the map
        // googleMap.clear()
        // add the new markers
        val marker: Marker = googleMap.addMarker(myMarker)
        marker.tag = locationData
    }

    private fun checkPermissionAndGetLocation() {
        Log.d(logTag, "Checking Permissions")
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted
            // check if we should ask
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // user already said no, don't ask again
                Toast.makeText(context,"We must access your location to plot where you are", Toast.LENGTH_SHORT).show()
            } else {
                // user hasn't previously declined, ask them
                val array: Array<String> = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissions(array, REQUEST_LOC_PERMISSION)
            }
        } else {
            // permission has been granted, do what we want
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, null)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOC_PERMISSION) {
            if (permissions.isNotEmpty() && permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionAndGetLocation()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data:
    Intent?) {
        if(resultCode != Activity.RESULT_OK) {
            return
        } else if(requestCode == REQUEST_LOC_ON) {
            locationUpdateState = true
            requireActivity().invalidateOptionsMenu()
        }
    }

    private fun checkIfLocationCanBeRetrieved() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            locationUpdateState = true
        }
        task.addOnFailureListener { exc ->
            locationUpdateState = false
            fab.isEnabled = false
            if (exc is ResolvableApiException) {
                try {
                    exc.startResolutionForResult(requireActivity(), REQUEST_LOC_ON)
                } catch (e: IntentSender.SendIntentException) {
                    // do nothing, they cancelled so ignore error
                }
            }
        }
    }
    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {
            return try {
                URL("https://api.openweathermap.org/data/2.5/onecall?lat=${lastLocation.latitude}&lon=${lastLocation.longitude}&appid=$API_KEY").readText(
                    Charsets.UTF_8
                )
            } catch (e: Exception) {
                null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

//            data class JsonData(
//                @SerializedName("weather")
//                var weather: String? = null,
//
//                @SerializedName("temp")
//                var temp: Double? = null) {}
//
//            val gson : Gson = GsonBuilder().setPrettyPrinting().create();
            /* Extracting JSON returns from the API */
            val jsonObj = JSONObject(result)

            val main = jsonObj.getJSONObject("current")
            val weather = main.getJSONArray("weather").getJSONObject(0)
            val weatherDescription = weather.getString("description")
            val temp = main.getString("temp")
            Toast.makeText(requireContext(), "Temperature: $temp Weather: $weatherDescription", Toast.LENGTH_SHORT).show()
            lastLocationData = LocationClass(lastLocation.latitude, lastLocation.longitude,
                BigDecimal(temp.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toDouble(),
                weatherDescription, getAddress(lastLocation))
            if(saveData) {
                mapViewModel.addLocation(
                    lastLocation,
                    getAddress(lastLocation),
                    weatherDescription,
                    BigDecimal(temp.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toDouble()
                )
            }
            updateUI(lastLocationData)
        }
    }
    companion object {
        const val REQUEST_LOC_ON = 0
    }
}
