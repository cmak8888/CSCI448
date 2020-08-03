package com.csci448.cmak_a2.ui.TheHistory

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.csci448.cmak_a2.data.Game

class HistoryPagerAdapter (fragment: Fragment,
                           private val games: List<Game>) : FragmentStateAdapter(fragment) {
    // override functions here
    override fun getItemCount(): Int {
        return games.size;
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = HistoryDetailFragment.newInstance(games[position].id)

        return fragment
    }
}