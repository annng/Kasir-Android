package com.artevak.kasirpos.ui.activity.profile.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.artevak.kasirpos.R
import com.artevak.kasirpos.data.model.Menu
import com.artevak.kasirpos.databinding.ItemMenuBinding

class ProfileMenuAdapter(
    private val items: ArrayList<Menu>,
    private val onItemClick: (position: Int) -> Unit
) :
    RecyclerView.Adapter<ProfileMenuAdapter.ViewHolder>() {

    lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_menu, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(items[position])
            holder.itemView.setOnClickListener { onItemClick(position) }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ItemViewHolder(private var itemBinding: ItemMenuBinding) : ViewHolder(itemBinding) {
        fun bind(item: Menu?) {
            itemBinding.executePendingBindings()
            itemBinding.item = item
        }
    }

    open class ViewHolder(viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root)

}