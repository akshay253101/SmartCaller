package com.beetlestance.smartcaller.ui.blocked.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.databinding.ItemViewBlockedBinding
import com.beetlestance.smartcaller.ui.blocked.BlockedViewModel
import com.beetlestance.smartcaller.utils.bindWithLayout

class BlockedContactsAdapter(private val viewModel: BlockedViewModel) :
    ListAdapter<BlockedContact, BlockedContactsAdapter.BlockedContactViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedContactViewHolder {
        return BlockedContactViewHolder(bindWithLayout(R.layout.item_view_blocked, parent))
    }

    override fun onBindViewHolder(holderBlockedContact: BlockedContactViewHolder, position: Int) {
        holderBlockedContact.binding.apply {
            blockedContact = getItem(position)
            blockedContactsViewModel = viewModel
            executePendingBindings()
        }
    }

    inner class BlockedContactViewHolder(val binding: ItemViewBlockedBinding) :
        RecyclerView.ViewHolder(binding.root)

    object DiffCallBack : DiffUtil.ItemCallback<BlockedContact>() {

        override fun areItemsTheSame(oldItem: BlockedContact, newItem: BlockedContact): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: BlockedContact, newItem: BlockedContact): Boolean {
            return oldItem == newItem
        }
    }
}