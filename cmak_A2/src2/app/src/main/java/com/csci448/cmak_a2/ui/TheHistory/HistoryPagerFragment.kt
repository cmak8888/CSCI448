package com.csci448.cmak_a2.ui.TheHistory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.csci448.cmak_a2.R
import com.csci448.cmak_a2.data.Game
import androidx.lifecycle.Observer
import java.util.*

private const val logTag = "csci448.HistoryPager"
private const val ARG_GAME_ID = "game_id"

class HistoryPagerFragment : Fragment() {

    private lateinit var adapter : HistoryPagerAdapter
    private lateinit var gameId : UUID
    private lateinit var gameViewPager : ViewPager2
    private lateinit var gamePagerViewModel : TheHistoryPagerViewModel

    private fun updateUI(games: List<Game>) {
        adapter = HistoryPagerAdapter(this, games)
        gameViewPager.adapter = adapter

        games.forEachIndexed { position, game ->
            if (game.id == gameId) {
                if (position == 0) {
                    gameViewPager.currentItem = position
                    return
                }
            }
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")

        val factory = TheHistoryPagerViewModelFactory(requireContext())
        gamePagerViewModel = ViewModelProvider(this, factory).get(TheHistoryPagerViewModel::class.java)
        gameId = arguments?.getSerializable(ARG_GAME_ID) as UUID
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(logTag, "onCreateView Called")
        val view = inflater.inflate(R.layout.fragment_pager, container, false)
        gameViewPager = view.findViewById(R.id.history_view_pager)
        updateUI(emptyList())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gamePagerViewModel.gameListLiveData.observe(
            viewLifecycleOwner,
            Observer { games ->
                games?.let {
                    Log.d(logTag, "Got ${games.size} crimes")
                    updateUI(games)
                }
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag,"onActivityCreated() called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart called")
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