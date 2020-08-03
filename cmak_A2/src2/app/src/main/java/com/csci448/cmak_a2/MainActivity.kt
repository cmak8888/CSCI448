package com.csci448.cmak_a2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.csci448.cmak_a2.R
import com.csci448.cmak_a2.data.Config
import com.csci448.cmak_a2.ui.Game.GameFragment
import com.csci448.cmak_a2.ui.Preferences.PreferencesFragment
import com.csci448.cmak_a2.ui.TheHistory.HistoryDetailFragment
import com.csci448.cmak_a2.ui.TheHistory.HistoryFragment
import com.csci448.cmak_a2.ui.TheHistory.HistoryPagerFragment
import com.csci448.cmak_a2.ui.Welcome.WelcomeFragment
import java.security.AccessController.getContext
import java.util.*

private const val EXTRA_FLAGS : String = "FLAGS"
private var prefFlags : Config = Config(true, false, false);

class MainActivity : AppCompatActivity(), WelcomeFragment.Callbacks, HistoryFragment.Callbacks, GameFragment.Callbacks{

    private val logTag = "448.MainActivity"
    private var detailContainer: FrameLayout? = null
    private lateinit var context : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        setContentView(R.layout.activity_main)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment == null) {
            val fragment = WelcomeFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }

    override fun onPlayGameSelected() {
        Log.d(logTag, "onPlayGameSelected: Opening Game")
        val fragment = GameFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit()
    }

    override fun onPrefSelected() {
        Log.d(logTag, "onPrefSelected: Opening Preferences")
        val fragment = PreferencesFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit()
    }

    override fun onHistorySelected() {
        Log.d(logTag, "onHistorySelected: Opening History")
        val fragment = HistoryFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit()
    }

    override fun onGameSelected(gameId: UUID) {
        Log.d(logTag, "onGameSElected: $gameId")
        if( detailContainer == null ) {
            val fragment = HistoryPagerFragment.newInstance(gameId)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(null).commit()
        } else {
            val fragment = HistoryDetailFragment.newInstance(gameId)
            supportFragmentManager.beginTransaction().replace(R.id.detail_fragment_container, fragment).commit()
        }
    }

    override fun returnToWelcome() {
        Log.d(logTag, "Return To Welcome: Welcome to welcome")
        val fragment = WelcomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "onResume() called")
    }

    override fun onPause() {
        Log.d(logTag, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(logTag, "onStop() called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy() called")
        super.onDestroy()
    }

    companion object {
        fun createIntent(packageContent: Context) : Intent{
            if (prefFlags.aiDiff == null) {
                return Intent(packageContent,MainActivity::class.java).apply {
                    putExtra(EXTRA_FLAGS, booleanArrayOf(prefFlags.playerNum, prefFlags.tile));
                }
            } else if (prefFlags.aiDiff == false) {
                return Intent(packageContent,MainActivity::class.java).apply {
                    putExtra(EXTRA_FLAGS, booleanArrayOf(prefFlags.playerNum, prefFlags.tile, false));
                }
            } else {
                return Intent(packageContent,MainActivity::class.java).apply {
                    putExtra(EXTRA_FLAGS, booleanArrayOf(prefFlags.playerNum, prefFlags.tile, true));
                }
            }
        }

        var pref : Config = Config(true, false, false);


        fun setPreferences(flags : Config) {
            //val sharedPref: SharedPreferences = context.getPreferences(Context.MODE_PRIVATE)
            pref = flags
//            val flag: Config = flags
//            val editor : SharedPreferences.Editor = sharedPref.edit()
//            editor.putBoolean("playerNum", flag.playerNum)
//            editor.putBoolean("tile", flag.tile)
//            if(flag.aiDiff == true) {
//                editor.putBoolean("aiDiff", true)
//            } else if(flag.aiDiff == false) {
//                editor.putBoolean("aiDiff", false)
//            } else {
//                editor.remove("aiDiff")
//            }
//            editor.apply()
        }
    }
}