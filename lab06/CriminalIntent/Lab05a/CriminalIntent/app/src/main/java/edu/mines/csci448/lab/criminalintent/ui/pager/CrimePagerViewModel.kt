package edu.mines.csci448.lab.criminalintent.ui.pager

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import edu.mines.csci448.lab.criminalintent.data.Crime
import edu.mines.csci448.lab.criminalintent.data.CrimeRepository

class CrimePagerViewModel (private val crimeRepository : CrimeRepository): ViewModel() {
    val crimes = crimeRepository.getCrimes()
    val crimeListLiveData: LiveData<PagedList<Crime>> = crimeRepository.getCrimes()

    fun getCrime(id: Int) {
        //return crimes[id]
    }
}