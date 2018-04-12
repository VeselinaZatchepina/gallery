package com.github.veselinazatchepina.mygallery.abstracts

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.mygallery.inflate

/**
 * Implementation of this adapter is used in RecyclerView.
 *
 * @param items list of entity
 * @param layoutResId layout for RecyclerView item
 */
abstract class AbstractAdapter<ITEM> constructor(
        protected var items: List<ITEM>,
        private val layoutResId: Int) : RecyclerView.Adapter<AbstractAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var viewHolder: Holder? = null
        val view = parent inflate layoutResId
        viewHolder = Holder(view)
        val itemView = viewHolder.itemView

        setClickListener(itemView, viewHolder)
        setLongClickListener(itemView, viewHolder)

        return viewHolder
    }

    private fun setClickListener(itemView: View, viewHolder: Holder) {
        itemView.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(itemView, adapterPosition)
            }
        }
    }

    private fun setLongClickListener(itemView: View, viewHolder: Holder) {
        itemView.setOnLongClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onLongItemClick(itemView, adapterPosition)
            }
            true
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    protected open fun onItemClick(itemView: View, position: Int) {

    }

    protected open fun onLongItemClick(itemView: View, position: Int) {

    }

    override fun getItemCount(): Int {
        return items.size
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