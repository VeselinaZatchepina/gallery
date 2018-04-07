package com.github.veselinazatchepina.mygallery.abstracts

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.mygallery.inflate


abstract class AbstractAdapter<ITEM> constructor(
        protected var items: List<ITEM>,
        private val layoutResId: Int) : RecyclerView.Adapter<AbstractAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var viewHolder: Holder? = null
        val view = parent inflate layoutResId
        viewHolder = Holder(view)
        val itemView = viewHolder.itemView
        itemView.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(itemView, adapterPosition)
            }
        }
        itemView.setOnLongClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onLongItemClick(itemView, adapterPosition)
            }
            true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    protected open fun onItemClick(itemView: View, position: Int) {

    }

    protected open fun onLongItemClick(itemView: View, position: Int) {

    }

    fun getAdapterItems() = items

    fun update(newItems: List<ITEM>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun addAll(newItems: List<ITEM>) {
        (items as ArrayList<ITEM>).addAll(newItems)
        notifyDataSetChanged()
    }
}