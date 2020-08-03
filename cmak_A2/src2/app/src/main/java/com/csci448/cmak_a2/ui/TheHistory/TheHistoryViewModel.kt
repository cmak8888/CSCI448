package com.csci448.cmak_a2.ui.TheHistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.csci448.cmak_a2.data.Game
import com.csci448.cmak_a2.data.GameRepository
import java.util.*

private const val log_tag = "448.HistoryViewModel"
class TheHistoryViewModel (private val gameRepository : GameRepository): ViewModel() {
    val games = gameRepository.getGames()
    var gameListLiveData: LiveData<PagedList<Game>> = gameRepository.getGames()
    var filterFlags : GameFilter = GameFilter(false,false,false,false)

    fun getGame(id: UUID) {
        gameRepository.getGame(id)
    }

    fun getGames() {
        gameListLiveData = gameRepository.getGames();
    }

    fun setFlags(flags : GameFilter) {
        filterFlags = flags
    }

    fun applyList(g : List<Game>) : List<Game> {
        val filteredGames : MutableList<Game> = mutableListOf()
        if(!filterFlags.filter_x && !filterFlags.filter_o && !filterFlags.filter_p1 && !filterFlags.filter_p2) {
            return g
        }
        for(i in g) {
            //X filter
            if(filterFlags.filter_x && filterFlags.filter_o && filterFlags.filter_p1 && filterFlags.filter_p2) {
                if((i.winningTile == "x" || i.winningTile == "o") && (i.winner == "Player Two" || i.winner == "Player One")) {
                    Log.d(log_tag, "adding x filter")
                    filteredGames.add(i)
                }
            } else
            if(filterFlags.filter_x && !filterFlags.filter_o && !filterFlags.filter_p1 && !filterFlags.filter_p2) {
                if(i.winningTile == "x") {
                    filteredGames.add(i)
                }
            } else if(filterFlags.filter_x && !filterFlags.filter_o && filterFlags.filter_p1 && !filterFlags.filter_p2) {
                if(i.winningTile == "x" && i.winner == "Player One") {
                    filteredGames.add(i)
                }
            } else if(filterFlags.filter_x && !filterFlags.filter_o && !filterFlags.filter_p1 && filterFlags.filter_p2) {
                if(i.winningTile == "x" && i.winner == "Player Two") {
                    filteredGames.add(i)
                }
            }else if(filterFlags.filter_x && !filterFlags.filter_o && filterFlags.filter_p1 && filterFlags.filter_p2) {
                if(i.winningTile == "x" && (i.winner == "Player Two" || i.winner == "Player One")) {
                    filteredGames.add(i)
                }
            } else
            //O Filter
            if(!filterFlags.filter_x && filterFlags.filter_o && !filterFlags.filter_p1 && !filterFlags.filter_p2) {
                if(i.winningTile == "o") {
                    filteredGames.add(i)
                }
            } else if(!filterFlags.filter_x && filterFlags.filter_o && filterFlags.filter_p1 && !filterFlags.filter_p2) {
                if(i.winningTile == "o" && i.winner == "Player One") {
                    filteredGames.add(i)
                }
            } else if(!filterFlags.filter_x && filterFlags.filter_o && !filterFlags.filter_p1 && filterFlags.filter_p2) {
                if(i.winningTile == "o" && i.winner == "Player Two") {
                    filteredGames.add(i)
                }
            }else if(!filterFlags.filter_x && filterFlags.filter_o && filterFlags.filter_p1 && filterFlags.filter_p2) {
                if(i.winningTile == "o" && (i.winner == "Player Two" || i.winner == "Player One")) {
                    filteredGames.add(i)
                }
            } else
            //X or O
            if(filterFlags.filter_x && filterFlags.filter_o && !filterFlags.filter_p1 && !filterFlags.filter_p2) {
                if(i.winningTile == "x" || i.winningTile == "o") {
                    filteredGames.add(i)
                }
            } else if(filterFlags.filter_x && filterFlags.filter_o && filterFlags.filter_p1 && !filterFlags.filter_p2) {
                if((i.winningTile == "x" || i.winningTile == "o") && i.winner == "Player One") {
                    filteredGames.add(i)
                }
            } else if(filterFlags.filter_x && filterFlags.filter_o && !filterFlags.filter_p1 && filterFlags.filter_p2) {
                if((i.winningTile == "x" || i.winningTile == "o") && i.winner == "Player Two") {
                    filteredGames.add(i)
                }
            } else
            //P1 Filter
            if(!filterFlags.filter_x && !filterFlags.filter_o && filterFlags.filter_p1 && !filterFlags.filter_p2) {
                if(i.winner == "Player One") {
                    filteredGames.add(i)
                }
            } else if(!filterFlags.filter_x && !filterFlags.filter_o && filterFlags.filter_p1 && filterFlags.filter_p2) {
                if(i.winner == "Player One" || i.winner == "Player Two") {
                    filteredGames.add(i)
                }
            } else
            //P2 Filter
            if(!filterFlags.filter_x && !filterFlags.filter_o && !filterFlags.filter_p1 && filterFlags.filter_p2) {
                if(i.winner == "Player Two") {
                    filteredGames.add(i)
                }
            }
        }
        return filteredGames
    }

//    fun getFilteredGames() {
//        var query : String = ""
//        val flags : MutableList<Any> = mutableListOf()
//        if(filterFlags.filter_o) {
//            query += "winningTile=?"
//            flags += "o"
//        }
//        if(filterFlags.filter_x) {
//            if(query.isNotEmpty()) {
//               query += " AND "
//            }
//            query += "winningTile=?"
//            flags += "x"
//        }
//        if(filterFlags.filter_p1) {
//            if(query.isNotEmpty()) {
//                query += " AND "
//            }
//            query += "winner=?"
//            flags += "p1"
//        }
//        if(filterFlags.filter_p2) {
//            if(query.isNotEmpty()) {
//                query += " AND "
//            }
//            query += "winner=?"
//            flags += "p2"
//        }
//
//        query = "SELECT * FROM game WHERE $query";
//        if(flags.size == 0) {
//            gameListLiveData = gameRepository.getGames()
//        } else if(flags.size == 1) {
//            val args : Array<Any> = arrayOf(flags[0])
//          //  gameListLiveData = listOf()
//            gameRepository.getGames(query, args)
//        } else if(flags.size == 2) {
//            val args : Array<Any> = arrayOf(flags[0],flags[1])
//            gameListLiveData = gameRepository.getGames(query, args)
//        }else if(flags.size == 3) {
//            val args : Array<Any> = arrayOf(flags[0],flags[1],flags[2])
//            gameListLiveData = gameRepository.getGames(query, args)
//        }else if(flags.size == 4) {
//            val args : Array<Any> = arrayOf(flags[0],flags[1],flags[2],flags[3])
//            gameListLiveData = gameRepository.getGames(query, args)
//        }
//    }
}