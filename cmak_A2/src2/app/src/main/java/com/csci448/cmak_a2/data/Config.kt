package com.csci448.cmak_a2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


class Config(     playerNumber : Boolean = true,
                  whichTile : Boolean = false,
                  aiDifficulty : Boolean?) {
    var playerNum = playerNumber
    var tile = whichTile
    var aiDiff = aiDifficulty
}