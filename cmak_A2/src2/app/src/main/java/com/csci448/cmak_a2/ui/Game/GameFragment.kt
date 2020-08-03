package com.csci448.cmak_a2.ui.Game

import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csci448.cmak_a2.MainActivity
import com.csci448.cmak_a2.R
import com.csci448.cmak_a2.data.Config
import com.csci448.cmak_a2.data.Game
import com.csci448.cmak_a2.ui.Game.GameViewModel
import com.csci448.cmak_a2.ui.Game.GameViewModelFactory
import com.csci448.cmak_a2.ui.Preferences.PreferencesViewModel
import com.csci448.cmak_a2.ui.Preferences.PreferencesViewModelFactory
import com.csci448.cmak_a2.ui.Welcome.WelcomeFragment
import java.lang.Math.random
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class GameTile(res: Int, tileLetter : String = "") {
    var resId : Int = res
    var letter : String = tileLetter

    fun clickedTile(id: Int, let : String) {
        if(resId == id) {
            letter = let;
        }
    }
}

class GameFragment : Fragment() {
    private val log_tag = "csci448.GameFrag"
    private lateinit var tileButtons : MutableList<Int>
    private var preferences : Config = Config(true, false, false)
    private var playerTurn = true                                   //Player 1 = true, Player 2 = false
    private var playerTile = "x"
    private var winner : String = "Player Null";
    private lateinit var tictactoeBoard : MutableList<GameTile>;
    private lateinit var playAgainButton : Button
    private lateinit var goBackButton : Button
    private lateinit var gameViewModel : GameViewModel
    private var freeze : Boolean = false
    private var tie : Boolean = false
    private var callbacks: Callbacks? = null

    interface Callbacks {
        fun returnToWelcome()
    }

    override fun onAttach(context : Context) {
        super.onAttach(context)
        Log.d(log_tag, "onAttach() called")
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(log_tag, "onCreated Called")

        val factory = GameViewModelFactory(requireContext())
        gameViewModel = ViewModelProvider(this, factory).get(GameViewModel::class.java)


        tictactoeBoard = mutableListOf()
        tileButtons = mutableListOf()

        //preferences = MainActivity.pref

        val sharedPref: SharedPreferences = this.activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        preferences.playerNum = sharedPref.getBoolean("playerNum", true)
        preferences.tile = sharedPref.getBoolean("tile", false)
        if(sharedPref.contains("aiDiff")) {
            preferences.aiDiff = sharedPref.getBoolean("aiDiff", false)
        }

        //Who goes first
        val i: Int = ThreadLocalRandom.current().nextInt(0, 2)
        if (i == 0) {
            playerTile = "x"
        } else {
            playerTile = "o"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_game, container, false)
        playAgainButton = view.findViewById(R.id.play_again_button)
        goBackButton = view.findViewById(R.id.go_back_button)
        Log.d(log_tag, "onCreateView() called")
        startGame(view)

        for(i in tileButtons) {
                val imgButton : ImageButton = view.findViewById(i)
                imgButton.setOnClickListener{
                    if(!freeze) {
                        updateGame(i, view)
                        if(preferences.playerNum && !freeze) {
                            updateGame(playAI(), view)
                        }
                    }
                }
        }

        playAgainButton.setOnClickListener{
            startGame(view)
            playAgainButton.visibility = View.GONE
            goBackButton.visibility = View.GONE
        }

        goBackButton.setOnClickListener{callbacks?.returnToWelcome()}
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(log_tag, "onViewCreated() called")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(log_tag,"onActivityCreated() called")
    }
    override fun onStart() {
        super.onStart()
        Log.d(log_tag, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(log_tag, "onResume() called")
    }

    override fun onPause() {
        Log.d(log_tag, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(log_tag, "onStop() called")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(log_tag, "onDestroyView called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(log_tag, "onDestroy() called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(log_tag, "onDetach() called")
        super.onDetach()
        callbacks = null
    }

    private fun startGame(view : View) {
        clear()
        tileButtons.add(0,R.id.tictac_1)
        tileButtons.add(1,R.id.tictac_2)
        tileButtons.add(2,R.id.tictac_3)
        tileButtons.add(3,R.id.tictac_4)
        tileButtons.add(4,R.id.tictac_5)
        tileButtons.add(5,R.id.tictac_6)
        tileButtons.add(6,R.id.tictac_7)
        tileButtons.add(7,R.id.tictac_8)
        tileButtons.add(8,R.id.tictac_9)
        tictactoeBoard.add(0,GameTile(R.id.tictac_1))
        tictactoeBoard.add(1,GameTile(R.id.tictac_2))
        tictactoeBoard.add(2,GameTile(R.id.tictac_3))
        tictactoeBoard.add(3,GameTile(R.id.tictac_4))
        tictactoeBoard.add(4,GameTile(R.id.tictac_5))
        tictactoeBoard.add(5,GameTile(R.id.tictac_6))
        tictactoeBoard.add(6,GameTile(R.id.tictac_7))
        tictactoeBoard.add(7,GameTile(R.id.tictac_8))
        tictactoeBoard.add(8,GameTile(R.id.tictac_9))
        for (i in tileButtons) {
            val button : ImageButton = view.findViewById(i)
            button.isClickable = true
            button.setImageResource(R.drawable.ic_blank)

        }
        if(!playerTurn && preferences.aiDiff != null) {
            updateGame(playAI(), view)
        }
    }

    private fun updateGame(placedTile : Int,view: View) {
        var imgButton : ImageButton = view.findViewById(placedTile)
        imgButton.isClickable = false
        if (playerTile == "x" && playerTurn) {
            val iconImg: Int = R.drawable.ic_squarex
            imgButton.setImageResource(iconImg)
        } else if (playerTile == "o" && !playerTurn) {
            val iconImg: Int = R.drawable.ic_squarex
            imgButton.setImageResource(iconImg)
        } else {
            val iconImg: Int = R.drawable.ic_squareo
            imgButton.setImageResource(iconImg)
        }
        val g : String = if(playerTurn && playerTile == "x") {
            "x"
        } else if (!playerTurn && playerTile == "o"){
            "x"
        } else {
            "o"
        }
        for(i in tictactoeBoard) {
            i.clickedTile(placedTile, g)
        }
        tileButtons.remove(placedTile)

        if(tileButtons.size == 0) {
            if(!checkWinner()) {
                tie = true
            }
            freeze = true
        }
        if(checkWinner() || tie){
            if(winner == "Player One") {
                Toast.makeText(context, R.string.p1win, Toast.LENGTH_SHORT).show() }
            else if(winner == "Player Two"){
                Toast.makeText(context, R.string.p2win, Toast.LENGTH_SHORT).show()
            } else {
                winner = "Tie"
                Toast.makeText(context, R.string.tie, Toast.LENGTH_SHORT).show()
            }

            val theGame = Game()
            theGame.gameTitle = "TicTacToe"
            theGame.winner = winner
            theGame.winningTile = if(winner == "Player One" && playerTile == "x") {
                "x"
            } else if (winner == "Player Two" && playerTile == "o") {
                "x"
            } else {
                "o"
            }
            Log.d(log_tag, "Saving the game")
            gameViewModel.saveGame(theGame)
            playAgainButton.visibility = View.VISIBLE
            goBackButton.visibility = View.VISIBLE
        } else {
            changePlayer()
        }
    }

    private fun changePlayer() {
        playerTurn = !playerTurn
    }

    private fun setWinner(ch : String) {
        Log.d(log_tag, "Setting Winner")
        freeze = true
        winner = if(preferences.tile && ch == "x") {
            "Player One"
        } else if(!preferences.tile && ch == "o") {
            "Player One"
        } else {
            "Player Two"
        }
    }

    private fun clear() {
        winner = "Player Null"
        tileButtons.clear()
        tictactoeBoard.clear()
        playAgainButton.visibility = View.GONE
        goBackButton.visibility = View.GONE
        freeze = false
        tie = false

    }

    private fun playAI() : Int {
        Log.d(log_tag, "AI is playing")
        if(preferences.aiDiff == false) {
            if(tileButtons.size == 0) {
                Log.d(log_tag, "Out of buttons Size: $tileButtons")
                return -1
            }
            return tileButtons[ThreadLocalRandom.current().nextInt(0,tileButtons.size)]
        } else if(preferences.aiDiff == true) {
            val space = findSpace()
            if (space == -1) {
                if(tileButtons.size == 0) {
                    Log.d(log_tag, "Out of buttons Size: $tileButtons")
                    return -1
                }
                return tileButtons[ThreadLocalRandom.current().nextInt(0,tileButtons.size)]
            } else {
                return tictactoeBoard[space].resId
            }
        }
        return -1
    }

    private fun checkWinner() : Boolean {
        var ch = "x"
        if(tictactoeBoard[0].letter == ch && tictactoeBoard[1].letter == ch && tictactoeBoard[2].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[3].letter == ch && tictactoeBoard[4].letter == ch && tictactoeBoard[5].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[6].letter == ch && tictactoeBoard[7].letter == ch && tictactoeBoard[8].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[0].letter == ch && tictactoeBoard[3].letter == ch && tictactoeBoard[6].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[1].letter == ch && tictactoeBoard[4].letter == ch && tictactoeBoard[7].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[2].letter == ch && tictactoeBoard[5].letter == ch && tictactoeBoard[8].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[0].letter == ch && tictactoeBoard[4].letter == ch && tictactoeBoard[8].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[2].letter == ch && tictactoeBoard[4].letter == ch && tictactoeBoard[6].letter == ch) {
            setWinner(ch)
            return true
        }
        ch = "o"
        if(tictactoeBoard[0].letter == ch && tictactoeBoard[1].letter == ch && tictactoeBoard[2].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[3].letter == ch && tictactoeBoard[4].letter == ch && tictactoeBoard[5].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[6].letter == ch && tictactoeBoard[7].letter == ch && tictactoeBoard[8].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[0].letter == ch && tictactoeBoard[3].letter == ch && tictactoeBoard[6].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[1].letter == ch && tictactoeBoard[4].letter == ch && tictactoeBoard[7].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[2].letter == ch && tictactoeBoard[5].letter == ch && tictactoeBoard[8].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[0].letter == ch && tictactoeBoard[4].letter == ch && tictactoeBoard[8].letter == ch) {
            setWinner(ch)
            return true
        } else if(tictactoeBoard[2].letter == ch && tictactoeBoard[4].letter == ch && tictactoeBoard[6].letter == ch) {
            setWinner(ch)
            return true
        }
        return false
    }

    private fun findSpace() : Int{
        if(tictactoeBoard[0].letter == playerTile && tictactoeBoard[0].letter == tictactoeBoard[1].letter && tictactoeBoard[2].letter == "" ) {
            return 2
        } else if (tictactoeBoard[1].letter == playerTile && tictactoeBoard[2].letter == tictactoeBoard[1].letter && tictactoeBoard[0].letter == "" ) {
            return 0
        } else if (tictactoeBoard[2].letter == playerTile && tictactoeBoard[2].letter == tictactoeBoard[0].letter && tictactoeBoard[1].letter == "" ) {
            return 1
        }else if (tictactoeBoard[0].letter == playerTile && tictactoeBoard[0].letter == tictactoeBoard[3].letter && tictactoeBoard[6].letter == "" ) {
            return 6
        }else if (tictactoeBoard[3].letter == playerTile && tictactoeBoard[3].letter == tictactoeBoard[6].letter && tictactoeBoard[0].letter == "" ) {
            return 0
        }else if (tictactoeBoard[6].letter == playerTile && tictactoeBoard[6].letter == tictactoeBoard[0].letter && tictactoeBoard[3].letter == "" ) {
            return 3
        }else if (tictactoeBoard[1].letter == playerTile && tictactoeBoard[4].letter == tictactoeBoard[1].letter && tictactoeBoard[7].letter == "" ) {
            return 7
        }else if (tictactoeBoard[4].letter == playerTile && tictactoeBoard[4].letter == tictactoeBoard[7].letter && tictactoeBoard[1].letter == "" ) {
            return 1
        }else if (tictactoeBoard[7].letter == playerTile && tictactoeBoard[7].letter == tictactoeBoard[1].letter && tictactoeBoard[4].letter == "" ) {
            return 4
        }else if (tictactoeBoard[2].letter == playerTile && tictactoeBoard[5].letter == tictactoeBoard[0].letter && tictactoeBoard[8].letter == "" ) {
            return 8
        }else if (tictactoeBoard[5].letter == playerTile && tictactoeBoard[5].letter == tictactoeBoard[8].letter && tictactoeBoard[2].letter == "" ) {
            return 2
        }else if (tictactoeBoard[8].letter == playerTile && tictactoeBoard[2].letter == tictactoeBoard[8].letter && tictactoeBoard[5].letter == "" ) {
            return 5
        }else if (tictactoeBoard[3].letter == playerTile && tictactoeBoard[3].letter == tictactoeBoard[4].letter && tictactoeBoard[5].letter == "" ) {
            return 5
        }else if (tictactoeBoard[4].letter == playerTile && tictactoeBoard[4].letter == tictactoeBoard[5].letter && tictactoeBoard[3].letter == "" ) {
            return 3
        }else if (tictactoeBoard[5].letter == playerTile && tictactoeBoard[5].letter == tictactoeBoard[3].letter && tictactoeBoard[4].letter == "" ) {
            return 4
        }else if (tictactoeBoard[6].letter == playerTile && tictactoeBoard[6].letter == tictactoeBoard[7].letter && tictactoeBoard[8].letter == "" ) {
            return 8
        }else if (tictactoeBoard[7].letter == playerTile && tictactoeBoard[7].letter == tictactoeBoard[8].letter && tictactoeBoard[6].letter == "" ) {
            return 6
        }else if (tictactoeBoard[8].letter == playerTile && tictactoeBoard[6].letter == tictactoeBoard[8].letter && tictactoeBoard[7].letter == "" ) {
            return 7
        }else if (tictactoeBoard[0].letter == playerTile && tictactoeBoard[0].letter == tictactoeBoard[4].letter && tictactoeBoard[8].letter == "" ) {
            return 8
        }else if (tictactoeBoard[4].letter == playerTile && tictactoeBoard[4].letter == tictactoeBoard[8].letter && tictactoeBoard[0].letter == "" ) {
            return 0
        }else if (tictactoeBoard[8].letter == playerTile && tictactoeBoard[2].letter == tictactoeBoard[8].letter && tictactoeBoard[4].letter == "" ) {
            return 4
        }else if (tictactoeBoard[2].letter == playerTile && tictactoeBoard[4].letter == tictactoeBoard[2].letter && tictactoeBoard[6].letter == "" ) {
            return 6
        }else if (tictactoeBoard[4].letter == playerTile && tictactoeBoard[4].letter == tictactoeBoard[6].letter && tictactoeBoard[2].letter == "" ) {
            return 2
        }else if (tictactoeBoard[6].letter == playerTile && tictactoeBoard[6].letter == tictactoeBoard[2].letter && tictactoeBoard[4].letter == "" ) {
            return 7
        }
        return -1
    }
}