package com.csci448.cmak_a2.ui.TheHistory

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csci448.cmak_a2.R
import com.csci448.cmak_a2.data.Game
import android.util.*

private const val Log_Tag = "448.HistoryHolder"

class HistoryHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var game: Game
    private val titleTextView: TextView = itemView.findViewById(R.id.game_title)
    private val dateTextView: TextView = itemView.findViewById(R.id.game_date)
    private val gameImageView: ImageView = itemView.findViewById(R.id.game_image)

    fun bind(game: Game, clickListener: (Game) -> Unit) {
        Log.d(Log_Tag, "Binding Holder")
        this.game = game
        itemView.setOnClickListener { clickListener(this.game) }
        titleTextView.text = this.game.gameTitle
        dateTextView.text = this.game.date.toString()
        if(this.game.gameTitle == "Tic-Tac-Toe") {
            gameImageView.setImageResource(R.drawable.ic_tictactoe)
        }
    }

    fun clear() {

    }
}