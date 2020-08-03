package com.csci448.cmak_a2.ui.Preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.csci448.cmak_a2.data.Config
import com.csci448.cmak_a2.data.Game
import com.csci448.cmak_a2.data.GameRepository

class PreferencesViewModel (private val gameRepository : GameRepository): ViewModel() {
    val games = gameRepository.getGames()
    val gameListLiveData: LiveData<PagedList<Game>> = gameRepository.getGames()

    var config : Config = Config(true, false, false)

    fun setPreferences(flags : Config) {
        config.playerNum = flags.playerNum
        config.tile = flags.tile
        config.aiDiff = flags.aiDiff
    }

    fun getPreferences() : Config{
        return config
    }

    fun clearAll() {
        gameRepository.clearAll()
    }
}