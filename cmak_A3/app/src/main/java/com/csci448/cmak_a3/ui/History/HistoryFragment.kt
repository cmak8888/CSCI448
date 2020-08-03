package com.csci448.cmak_a3.ui.History

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci448.cmak_a3.R
import com.csci448.cmak_a3.data.Location

private const val logTag = "448.History"
class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        val factory = HistoryViewModelFactory(requireContext())
        historyViewModel = ViewModelProvider(this, factory).get(HistoryViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        historyRecyclerView = view.findViewById(R.id.history_recycler_view)
        historyRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = HistoryAdapter(historyViewModel) { location:Location -> Unit
            Log.d(logTag, "Log Tag ${location.Address}")
        }
        historyRecyclerView.adapter = adapter

        val itemTouchHelperCallback = SwipeToDeleteHelper(adapter)
        val touchHelper = ItemTouchHelper(itemTouchHelperCallback)
        touchHelper.attachToRecyclerView(historyRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")

        historyViewModel.locationLiveData.observe(
            viewLifecycleOwner,
            Observer { locations ->
                locations?.let {
                    Log.d(logTag, "Got locations ${locations.size}")
                    updateUI(locations)
                }
            }
        )
    }

    private fun updateUI(locations: PagedList<Location>) {
        Log.d(logTag, "Updating UI")
        adapter.submitList(locations)
    }
}
