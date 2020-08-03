package com.csci448.cmak_a2.ui.TheHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.csci448.cmak_a2.data.Game
import com.csci448.cmak_a2.data.GameRepository
import java.util.*


class TheHistoryDetailViewModel (private val gameRepository : GameRepository): ViewModel() {
    var gameIdLiveData = MutableLiveData<UUID>()

    var gameLiveData: LiveData<Game?> =
        Transformations.switchMap(gameIdLiveData) { gameId -> gameRepository.getGame(gameId)}
    fun loadGame(gameId: UUID) {
        gameIdLiveData.value = gameId
    }
}