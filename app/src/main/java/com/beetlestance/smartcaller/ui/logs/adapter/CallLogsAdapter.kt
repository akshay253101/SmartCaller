package com.beetlestance.smartcaller.ui.logs.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.data.states.CallLog
import com.beetlestance.smartcaller.databinding.ItemViewCallLogsBinding
import com.beetlestance.smartcaller.ui.logs.CallLogsViewModel
import com.beetlestance.smartcaller.utils.bindWithLayout

class CallLogsAdapter(private val viewModel: CallLogsViewModel) :
    PagingDataAdapter<CallLog, CallLogsAdapter.ContactViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(bindWithLayout(R.layout.item_view_call_logs, parent))
    }

    override fun onBindViewHolder(holderContact: ContactViewHolder, position: Int) {
        holderContact.binding.apply {
            callLog = getItem(position)
            callLogsViewModel = viewModel
            executePendingBindings()
        }
    }

    inner class ContactViewHolder(val binding: ItemViewCallLogsBinding) :
        RecyclerView.ViewHolder(binding.root)

    object DiffCallBack : DiffUtil.ItemCallback<CallLog>() {

        override fun areItemsTheSame(oldItem: CallLog, newItem: CallLog): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: CallLog, newItem: CallLog): Boolean {
            return oldItem == newItem
        }
    }
}