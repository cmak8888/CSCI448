package com.csci448.cmak_a2.ui.TheHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.csci448.cmak_a2.R
import com.csci448.cmak_a2.data.Game

class HistoryListAdapter(private val games: List<Game>, private val clickListener: (Game) -> Unit): PagedListAdapter<Game, HistoryHolder>(DIFF_CALLBACK) {
    override fun getItemCount(): Int {
        return games.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_history_item, parent, false)
        return HistoryHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        val game = games[position]
        if(game != null) {
            holder.bind(game, clickListener)
        } else {
            holder.clear()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game) =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Game, newItem: Game) =
                oldItem == newItem
        }
    }
}