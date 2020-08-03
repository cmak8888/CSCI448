package com.csci448.cmak_a3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Location(val Latitude: Double,
                    val Longitude: Double,
                    val Temperature: Double,
                    val Weather: String,
                    val Address: String,
                    val time: Date = Date(),
                    @PrimaryKey val id: UUID = UUID.randomUUID()) : Serializable