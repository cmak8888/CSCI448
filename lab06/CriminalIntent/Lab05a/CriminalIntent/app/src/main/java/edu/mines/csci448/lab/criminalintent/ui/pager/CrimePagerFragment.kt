package edu.mines.csci448.lab.criminalintent.ui.pager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import edu.mines.csci448.lab.criminalintent.R
import edu.mines.csci448.lab.criminalintent.data.Crime
import kotlinx.android.synthetic.main.fragment_pager.view.*
import java.util.*


private const val ARG_CRIME_ID = "crime_id"

class CrimePagerFragment : Fragment() {

    private val logTag = "csci448.CrimePager"
    private lateinit var crimeViewPager: ViewPager2
    private lateinit var crimePagerViewModel : CrimePagerViewModel
    private lateinit var adapter : CrimePagerAdapter
    private lateinit var crimeId : UUID

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")

        val factory = CrimePagerViewModelFactory(requireContext())
        crimePagerViewModel = ViewModelProvider(this, factory).get(CrimePagerViewModel::class.java)
        crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(logTag, "onCreateView Called")
        val view = inflater.inflate(R.layout.fragment_pager, container, false)
        crimeViewPager = view.findViewById(R.id.crime_view_pager)
        updateUI(emptyList())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimePagerViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.d(logTag, "Got ${crimes.size} crimes")
                    updateUI(crimes)
                }
            }
        )
    }
    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimePagerAdapter(this, crimes)
        crimeViewPager.adapter = adapter

        crimes.forEachIndexed { position, crime ->
            if (crime.id == crimeId) {
                if (position == 0) {
                    crimeViewPager.currentItem = position
                    return
                }
            }
        }
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.d(logTag, "onOptionsItemSelected() called")
//        return when (item.itemId) {
//            R.id.first_crime_item -> {
//                val crime = crimePagerViewModel.crimes.getCrime(0)
//                if (crime.id == crimeId) {
//
//                }
//                true
//            }
//            else -> {super.onOptionsItemSelected(item)}
//        }
//    }
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
        fun newInstance(crimeId: UUID): CrimePagerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimePagerFragment().apply {
                arguments = args
            }
        }
    }

}