package com.csci448.cmak_a2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Game(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var gameTitle: String = "",
                 var winner: String = "",
                 var winningTile: String = "",
                 var date: Date = Date()
)