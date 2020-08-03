package com.csci448.cmak_a2.ui.Preferences

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.blue
import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.csci448.cmak_a2.MainActivity
import com.csci448.cmak_a2.R
import com.csci448.cmak_a2.data.Config
import com.csci448.cmak_a2.ui.Welcome.WelcomeFragment
import java.util.*
import kotlin.system.exitProcess

class PreferencesFragment : Fragment() {

    private val log_tag = "csci448.preferences"
    private lateinit var numPlayers1 : Button;
    private lateinit var numPlayers2 : Button;
    private lateinit var aiLayout : FrameLayout;
    private lateinit var aiRadio : RadioGroup;
    private lateinit var normalButton : RadioButton;
    private lateinit var hardButton : RadioButton
    private lateinit var xButton : Button;
    private lateinit var oButton : Button;
    private lateinit var clearButton : Button;
    private val selectedColor : Int = Color.rgb(254,216,177)
    private val nonSelectedColor : Int = Color.WHITE;
    private lateinit var prefViewModel : PreferencesViewModel
    private lateinit var flags : Config

    private fun clearData() {
        Toast.makeText(this.context, "Your Data has been cleared.", Toast.LENGTH_SHORT).show()
        prefViewModel.clearAll()
    }

    override fun onAttach(context : Context) {
        super.onAttach(context)
        Log.d(log_tag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(log_tag, "onCreated Called")
        val factory = PreferencesViewModelFactory(requireContext())
        prefViewModel = ViewModelProvider(this, factory).get(PreferencesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_preferences, container, false)
        Log.d(log_tag, "onCreateView() called")

        //flags = prefViewModel.getPreferences();
        flags = MainActivity.pref;
        numPlayers1 = view.findViewById(R.id.one_player)
        numPlayers2 = view.findViewById(R.id.two_player)
        aiLayout = view.findViewById(R.id.ai_difficulty)
        aiRadio = view.findViewById(R.id.difficulty_radiogroup)
        normalButton = view.findViewById(R.id.normal_button)
        hardButton = view.findViewById(R.id.hard_button)
        xButton = view.findViewById(R.id.x_button)
        oButton = view.findViewById(R.id.o_button)
        clearButton = view.findViewById(R.id.clear_button)

        if(flags.playerNum) {
            numPlayers1.setBackgroundColor(selectedColor);
            numPlayers2.setBackgroundColor(nonSelectedColor);
            aiLayout.visibility = View.VISIBLE;
        } else {
            numPlayers2.setBackgroundColor(selectedColor)
            numPlayers1.setBackgroundColor(nonSelectedColor)
            aiLayout.visibility = View.GONE;
        }

        if(flags.aiDiff == true) {
            hardButton.isChecked = true
        } else if (flags.aiDiff == false) {
            normalButton.isChecked = true
        }

        if(!flags.tile) {
            xButton.setBackgroundColor(selectedColor)
            oButton.setBackgroundColor(nonSelectedColor)
        } else {
            oButton.setBackgroundColor(selectedColor)
            xButton.setBackgroundColor(nonSelectedColor)
        }


        numPlayers1.setOnClickListener{
            numPlayers1.setBackgroundColor(selectedColor);
            numPlayers2.setBackgroundColor(nonSelectedColor)
            aiLayout.visibility = View.VISIBLE;
            flags.playerNum = true
        }
        numPlayers2.setOnClickListener{
            numPlayers2.setBackgroundColor(selectedColor)
            numPlayers1.setBackgroundColor(nonSelectedColor)
            aiLayout.visibility = View.GONE;
            flags.playerNum = false
        }

        normalButton.setOnClickListener{ flags.aiDiff = false }
        hardButton.setOnClickListener{ flags.aiDiff = true}
        xButton.setOnClickListener{
            xButton.setBackgroundColor(selectedColor)
            oButton.setBackgroundColor(nonSelectedColor)
            flags.tile = false
        }
        oButton.setOnClickListener{
            oButton.setBackgroundColor(selectedColor)
            xButton.setBackgroundColor(nonSelectedColor)
            flags.tile = true
        }
        clearButton.setOnClickListener{clearData()}

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
        //prefViewModel.setPreferences(flags)

        val sharedPref: SharedPreferences = this.activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val flag: Config = flags
        val editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean("playerNum", flag.playerNum)
        editor.putBoolean("tile", flag.tile)
        if(flag.aiDiff == true) {
            editor.putBoolean("aiDiff", true)
        } else if(flag.aiDiff == false) {
            editor.putBoolean("aiDiff", false)
        } else {
            editor.remove("aiDiff")
        }
        editor.apply()
        MainActivity.setPreferences(flags);
        Toast.makeText(this.context, "Your Preferences have been saved.", Toast.LENGTH_SHORT).show()
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
    }
}