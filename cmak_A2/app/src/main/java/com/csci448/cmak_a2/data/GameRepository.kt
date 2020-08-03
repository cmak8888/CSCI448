package com.csci448.cmak_a2.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SimpleSQLiteQuery
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GameRepository(private val gameDao: GameDao) {

    private val executor = Executors.newSingleThreadExecutor()

    fun getGames(): LiveData<PagedList<Game>> {
        return LivePagedListBuilder(
            gameDao.getGames(),
            PagedList.Config.Builder().setPageSize(125).setPrefetchDistance(50).setMaxSize(400).build()
        ).build()
    }

//    fun getGames(query : String, str: Array<Any>): LiveData<PagedList<Game>> {
//        return LivePagedListBuilder(
//            gameDao.getGames(SimpleSQLiteQuery(query, str)),
//            PagedList.Config.Builder().setPageSize(125).setPrefetchDistance(50).setMaxSize(400).build()
//        ).build()
//    }

    fun getGame(id: UUID): LiveData<Game?> {
        return gameDao.getGame(id)
    }

    fun saveGame(game: Game) {
        executor.execute {
            gameDao.saveGame(game)
        }
    }

    fun deleteGame(game: Game) {
        executor.execute {
            gameDao.deleteGame(game)
        }
    }

    fun clearAll() {
        executor.execute {
            gameDao.clearAll()
        }
    }

    companion object {
        private var instance: GameRepository? = null
        fun getInstance(context: Context): GameRepository? {
            return instance ?: let {
                if (instance == null) {
                    val database = GameDatabase.getInstance(context)
                    instance = GameRepository(database.gameDao())
                }
                return instance
            }
        }
    }
}