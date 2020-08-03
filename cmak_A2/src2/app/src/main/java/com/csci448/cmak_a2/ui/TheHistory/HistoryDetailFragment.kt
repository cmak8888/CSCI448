package com.csci448.cmak_a2.ui.TheHistory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.csci448.cmak_a2.R
import com.csci448.cmak_a2.data.Game
import java.util.*

private const val LOG_TAG = "448.HistoryDetail"
private const val ARG_GAME_ID = "game_id"

class HistoryDetailFragment : Fragment() {

    private lateinit var game: Game
    private lateinit var titleField: TextView
    private lateinit var playerWinner: TextView
    private lateinit var tileWinner: TextView
    private lateinit var dateButton : Button
    private lateinit var gameDetailViewModel: TheHistoryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate() called")
        game = Game()

        val factory = TheHistoryDetailViewModelFactory(requireContext())
        gameDetailViewModel = ViewModelProvider(this, factory).get(TheHistoryDetailViewModel::class.java)

        val gameId: UUID = arguments?.getSerializable(ARG_GAME_ID) as UUID
        gameDetailViewModel.loadGame(gameId)
        Log.d(LOG_TAG, "args bundle game ID: $gameId")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(LOG_TAG,"onCreateView() called")
        val view : View = inflater.inflate(R.layout.activity_historydetail, container, false)
        titleField = view.findViewById(R.id.game_title_label)
        playerWinner = view.findViewById(R.id.player_winner)
        tileWinner = view.findViewById(R.id.tile_winner)
        dateButton = view.findViewById(R.id.game_date_button)

        dateButton.apply {
            text = game.date.toString()
            isEnabled = false
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailViewModel.gameLiveData.observe(
            viewLifecycleOwner,
            Observer {game -> game?.let {
                this.game = game
                updateUI()
            }})
        Log.d(LOG_TAG, "onViewCreated() called")
    }

    private fun updateUI() {
        titleField.text = game.gameTitle
        playerWinner.text = game.winner
        tileWinner.text = game.winningTile
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(LOG_TAG,"onActivityCreated() called")
    }
    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume() called")
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(LOG_TAG, "onDestroyView called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(LOG_TAG, "onDetach() called")
        super.onDetach()
    }

    companion object {
        fun newInstance(gameId: UUID): HistoryDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_GAME_ID, gameId)
            }
            return HistoryDetailFragment().apply {
                arguments = args
            }
        }
    }
}