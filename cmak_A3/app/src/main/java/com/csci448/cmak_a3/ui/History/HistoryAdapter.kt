package com.csci448.cmak_a3.ui.History

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.csci448.cmak_a3.R
import com.csci448.cmak_a3.data.Location

class HistoryAdapter(private val historyViewModel: HistoryViewModel, private val clickListener: (Location) -> Unit ) : PagedListAdapter<Location, HistoryViewHolder>(DIFF_UTIL),
    SwipeToDeleteHelper.ItemTouchHelperAdapter {

    private lateinit var attachedRecyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val location = getItem(position)
        if(location != null ) {
            holder.bind(location, clickListener)
        } else {
            holder.clear()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachedRecyclerView = recyclerView
    }

    @SuppressLint("StringFormatInvalid")
    override fun onItemDismiss(position: Int) {
        val context = attachedRecyclerView.context
        val location = getItem(position)
        if (location != null) {
            AlertDialog.Builder(context)
                .setTitle(R.string.confirm_delete)
                .setMessage(context.resources.getString(R.string.confirm_delete_message, location.Address))
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    historyViewModel.deleteLocation(location)
                }
                .setNegativeButton(android.R.string.no) { _, _ -> notifyItemChanged(position) }
                .show()
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Location>() {
            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem == newItem

            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem.id == newItem.id
        }
    }
}