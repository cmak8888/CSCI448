package com.csci448.cmak_a2.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import java.util.*

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getGames(): DataSource.Factory<Int, Game>

    @Query("SELECT * FROM game WHERE id=(:id)")
    fun getGame(id: UUID): LiveData<Game?>

//    @RawQuery
//    fun getGames(query: SupportSQLiteQuery)

    @Update
    fun updateGame(game: Game)

    @Insert
    fun saveGame(game: Game)

    @Delete
    fun deleteGame(game: Game)

    @Query("DELETE FROM game")
    fun clearAll()

   // @Delete
   // fun clearAllTables()
}