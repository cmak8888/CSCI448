package com.csci448.cmak_a2.ui.Welcome

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.csci448.cmak_a2.R
import kotlin.system.exitProcess

class WelcomeFragment: Fragment() {

    private val log_tag = "csci448.Welcome"

    private lateinit var playGameButton : Button
    //private lateinit var playImageGameButton : ImageButton
    private lateinit var preferencesButton : Button
    private lateinit var historyButton : Button
    private lateinit var exitButton : Button

    interface Callbacks{
        fun onHistorySelected()
        fun onPrefSelected()
        fun onPlayGameSelected()
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context : Context) {
        super.onAttach(context)
        Log.d(log_tag, "onAttach() called")


        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(log_tag, "onCreated Called")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_welcome, container, false)
        Log.d(log_tag, "onCreateView() called")

        playGameButton = view.findViewById(R.id.play_game_button)
        //playImageGameButton = view.findViewById(R.id.play_image_button)
        historyButton = view.findViewById(R.id.history_game_button)
        preferencesButton = view.findViewById(R.id.preferences_game_button)
        exitButton = view.findViewById(R.id.exit_game_button)


        playGameButton.setOnClickListener{callbacks?.onPlayGameSelected()}
        //playImageGameButton.setOnClickListener{callbacks?.onPlayGameSelected()}
        historyButton.setOnClickListener{callbacks?.onHistorySelected()}
        preferencesButton.setOnClickListener{callbacks?.onPrefSelected()}
        exitButton.setOnClickListener{exitProcess(-1)}

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(log_tag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.welcome_header, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(log_tag, "onOptionsItemSelected() called")
        return when (item.itemId) {
            R.id.play_game_button -> {
                callbacks?.onPlayGameSelected()
                true
            }
            R.id.preferences_button -> {
                callbacks?.onPrefSelected()
                true
            }
            R.id.history_button -> {
                callbacks?.onHistorySelected()
                true
            }
            R.id.exit_button -> {
                exitProcess(-1)
            }
            else -> super.onOptionsItemSelected(item)
        }
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

}