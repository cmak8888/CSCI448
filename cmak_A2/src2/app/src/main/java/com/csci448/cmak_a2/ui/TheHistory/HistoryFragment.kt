package com.csci448.cmak_a2.ui.TheHistory

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci448.cmak_a2.R
import com.csci448.cmak_a2.data.Game
import java.util.*
import kotlin.system.exitProcess

class HistoryFragment : Fragment() {
    interface Callbacks {
        fun onGameSelected(gameId: UUID)
        fun onPrefSelected()
        fun onPlayGameSelected()
    }

    private var callbacks: Callbacks? = null
    private val logTag = "448.HistoryListFrag"
    private lateinit var historyListViewModel: TheHistoryViewModel
    private lateinit var gameRecyclerView: RecyclerView
    private lateinit var adapter: HistoryListAdapter
    private var filterFlags : GameFilter = GameFilter(false, false, false,false)

    private fun updateUI(games: List<Game>) {
        val filteredGames: List<Game> = historyListViewModel.applyList(games)
        adapter = HistoryListAdapter(filteredGames) {
                game: Game -> Unit
            callbacks?.onGameSelected(game.id)

        }
        gameRecyclerView.adapter = adapter
    }

    private fun clearList() {
        updateUI(emptyList())
    }

    private fun initList() {
        historyListViewModel.gameListLiveData.observe(viewLifecycleOwner,
            Observer{
                games -> games?.let {
                    Log.i(logTag, "Got Games ${games.size}")
                    updateUI(games)
                }
            })
    }

    override fun onAttach(context : Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        setHasOptionsMenu(true)
        val factory = TheHistoryViewModelFactory(requireContext())
        historyListViewModel = ViewModelProvider(this, factory).get(TheHistoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_historylist, container, false)
        gameRecyclerView = view.findViewById(R.id.history_list_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)
        updateUI(emptyList())
        Log.d(logTag,"onCreateView() called")
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.history_header, menu)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
        historyListViewModel.gameListLiveData.observe(viewLifecycleOwner,
            Observer{
                    games -> games?.let {
                Log.i(logTag, "Got Games ${games.size}")
                updateUI(games)
            }
            })

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag,"onActivityCreated() called")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(logTag, "onOptionsItemSelected() called")
        return when (item.itemId) {
            R.id.o_filter_button -> {
                Log.d(logTag, "Applying O Filter")
                filterFlags.filter_o = !filterFlags.filter_o
                item.isChecked = filterFlags.filter_o
                historyListViewModel.setFlags(filterFlags)
                //historyListViewModel.getFilteredGames()
                clearList()
                initList()
                true
            }
            R.id.x_filter_button -> {
                Log.d(logTag, "Applying X Filter")
                filterFlags.filter_x = !filterFlags.filter_x
                item.isChecked = filterFlags.filter_x
                historyListViewModel.setFlags(filterFlags)
                //historyListViewModel.getFilteredGames()
                clearList()
                initList()
                true
            }
            R.id.p1_filter_button -> {
                Log.d(logTag, "Applying One Filter")
                filterFlags.filter_p1 = !filterFlags.filter_p1
                item.isChecked = filterFlags.filter_p1
                historyListViewModel.setFlags(filterFlags)
                //historyListViewModel.getFilteredGames()
                clearList()
                initList()
                true
            }
            R.id.p2_filter_button -> {
                Log.d(logTag, "Applying Two Filter")
                filterFlags.filter_p2 = !filterFlags.filter_p2
                item.isChecked = filterFlags.filter_p2
                historyListViewModel.setFlags(filterFlags)
                //historyListViewModel.getFilteredGames()
                clearList()
                initList()
                true
            }
            R.id.play_game_button -> {
                callbacks?.onPlayGameSelected()
                true
            }
            R.id.preferences_button -> {
                callbacks?.onPrefSelected()
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

    override fun onDestroyView() {
        Log.d(logTag, "onDestroyView called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy() called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(logTag, "onDetach() called")
        super.onDetach()

        callbacks = null
    }
}