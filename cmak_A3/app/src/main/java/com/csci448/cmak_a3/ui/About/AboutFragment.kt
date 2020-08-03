package com.csci448.cmak_a3.ui.About

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.csci448.cmak_a3.R

private const val logTag = "448.About"
class AboutFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d(logTag, "onCreateView")
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        return view
    }
}
