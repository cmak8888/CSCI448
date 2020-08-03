package edu.mines.csci448.lab.criminalintent.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.mines.csci448.lab.criminalintent.R
import edu.mines.csci448.lab.criminalintent.data.Crime

class CrimeListAdapter(private val crimes: List<Crime>, private val clickListener : (Crime) -> Unit) : PagedListAdapter<Crime, CrimeHolder>(DIFF_CALLBACK) {
    override fun getItemCount(): Int {
        return crimes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_crime, parent, false)
        return CrimeHolder(view)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        if(crime != null) {
            holder.bind(crime, clickListener)
        } else {
            holder.clear()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Crime>() {
            override fun areItemsTheSame(oldItem: Crime, newItem: Crime) =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Crime, newItem: Crime) =
                oldItem == newItem
        }
    }
}