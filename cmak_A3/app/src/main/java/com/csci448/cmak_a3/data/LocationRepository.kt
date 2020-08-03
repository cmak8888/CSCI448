package com.csci448.cmak_a3.data


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.csci448.cmak_a3.data.Location
import java.util.*
import java.util.concurrent.Executors

class LocationRepository private constructor(private val locationDao: LocationDao) {

    companion object {
        private var instance: LocationRepository? = null

        fun getInstance(context: Context): LocationRepository? {
            return instance ?: let {
                if(instance == null) {
                    val database = LocationDatabase.getInstance(context)
                    instance = LocationRepository(database.locationDao())
                }
                return instance
            }
        }
    }

    private val executor = Executors.newSingleThreadExecutor()

    fun getLocations(): LiveData<PagedList<Location>> {
        return LivePagedListBuilder(
            locationDao.getLocations(),
            PagedList.Config.Builder().setPageSize(100).build()
        ).build()
    }

    fun getLocation(id: UUID): LiveData<Location?> = locationDao.getLocation(id)

    fun getLocationByAddress(address: String) = locationDao.getLocationByAddress(address)

    fun addLocation(location: Location) {
        executor.execute {
            locationDao.addLocation(location)
        }
    }

    fun deleteLocation(location: Location) {
        executor.execute {
            locationDao.deleteLocation(location)
        }
    }

    fun clear() {
        executor.execute {
            locationDao.clear()
        }
    }

}