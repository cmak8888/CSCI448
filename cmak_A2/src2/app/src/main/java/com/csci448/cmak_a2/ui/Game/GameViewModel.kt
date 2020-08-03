package com.csci448.cmak_a2.ui.Game

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.csci448.cmak_a2.data.Game
import com.csci448.cmak_a2.data.GameRepository

class GameViewModel (private val gameRepository : GameRepository): ViewModel() {
    val games = gameRepository.getGames()
    val gameListLiveData: LiveData<PagedList<Game>> = gameRepository.getGames()

    fun saveGame(game: Game) {
        gameRepository.saveGame(game)
    }
}