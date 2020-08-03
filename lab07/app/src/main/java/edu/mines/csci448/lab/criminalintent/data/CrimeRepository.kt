package edu.mines.csci448.lab.criminalintent.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import java.util.*
import java.util.concurrent.Executors

class CrimeRepository(private val crimeDao: CrimeDao) {

    private val executor = Executors.newSingleThreadExecutor()

    fun getCrimes(): LiveData<PagedList<Crime>> {
        return LivePagedListBuilder(
            crimeDao.getCrimes(),
            PagedList.Config.Builder().setPageSize(125).setPrefetchDistance(50).setMaxSize(400).build()
        ).build()
    }

    fun getCrime(id: UUID): LiveData<Crime?> {
        return crimeDao.getCrime(id)
    }

    fun updateCrime(crime: Crime) {
        executor.execute{
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }

    fun delCrime(crime: Crime) {
        executor.execute {
            crimeDao.delCrime(crime)
        }
    }

    companion object {
        private var instance: CrimeRepository? = null
        fun getInstance(context: Context): CrimeRepository? {
            return instance ?: let {
                if (instance == null) {
                    val database = CrimeDatabase.getInstance(context)
                    instance = CrimeRepository(database.crimeDao())
                }
                return instance
            }
        }
    }
}