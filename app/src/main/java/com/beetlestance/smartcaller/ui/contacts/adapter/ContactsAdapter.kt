package com.beetlestance.smartcaller.ui.contacts.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.databinding.ItemViewContactBinding
import com.beetlestance.smartcaller.ui.contacts.ContactsViewModel
import com.beetlestance.smartcaller.utils.bindWithLayout

class ContactsAdapter(private val viewModel: ContactsViewModel) :
    PagingDataAdapter<Contact, ContactsAdapter.ContactViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(bindWithLayout(R.layout.item_view_contact, parent))
    }

    override fun onBindViewHolder(holderContact: ContactViewHolder, position: Int) {
        holderContact.binding.apply {
            contact = getItem(position)
            contactsViewModel = viewModel
            executePendingBindings()
        }
    }

    inner class ContactViewHolder(val binding: ItemViewContactBinding) :
        RecyclerView.ViewHolder(binding.root)

    object DiffCallBack : DiffUtil.ItemCallback<Contact>() {

        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}