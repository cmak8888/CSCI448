package com.csci448.cmak_a3.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.csci448.cmak_a3.data.Location
import java.util.*
import androidx.paging.DataSource

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    fun getLocations(): DataSource.Factory<Int, Location>

    @Query("SELECT * FROM location WHERE id=(:id)")
    fun getLocation(id: UUID): LiveData<Location?>

    @Query("SELECT * FROM location WHERE Address=(:address)")
    fun getLocationByAddress(address: String): LiveData<Location?>

    @Insert
    fun addLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)

    @Query("DELETE FROM location")
    fun clear()
}