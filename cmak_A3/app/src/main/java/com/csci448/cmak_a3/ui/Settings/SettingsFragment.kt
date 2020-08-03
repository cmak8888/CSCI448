package com.csci448.cmak_a3.ui.Settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.csci448.cmak_a3.R
import com.csci448.cmak_a3.ui.Settings.SettingsViewModelFactory
import com.csci448.cmak_a3.ui.Settings.SettingsViewModel

private const val logTag = "448.Settings"

class SettingsFragment : Fragment() {

    private var saveData = false
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var switch: Switch
    private lateinit var deleteButton :ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(logTag, "onCreate")
        super.onCreate(savedInstanceState)

        val factory = SettingsViewModelFactory(requireContext())
        settingsViewModel =
            ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        switch = view.findViewById(R.id.toggleSave)

        switch.setOnClickListener {
            saveData = switch.isChecked
            val sharedPref: SharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putBoolean("saveData", saveData)
            editor.apply()
        }
        deleteButton = view.findViewById(R.id.delete_setting)

        deleteButton.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.deletedata_settings)
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    settingsViewModel.clear()
                    val sharedPref: SharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context)
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putBoolean("delete", true)
                    editor.apply()
                }
                .setNegativeButton(android.R.string.no) {dialog, which -> dialog.cancel() }
                .show()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context) ?: return
        saveData = sharedPref.getBoolean("saveData", true)
        switch.isChecked = saveData
    }

    override fun onStop() {
        Log.d(logTag, "onStop")
        super.onStop()
    }
}
