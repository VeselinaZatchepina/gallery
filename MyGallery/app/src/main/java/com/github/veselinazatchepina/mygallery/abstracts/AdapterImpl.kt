package com.github.veselinazatchepina.mygallery.abstracts

import android.view.View


class AdapterImpl<ITEM>(items: List<ITEM>,
                        layoutResId: Int,
                        private val bindHolder: View.(ITEM) -> Unit
) : AbstractAdapter<ITEM>(items, layoutResId) {

    private var itemClick: ITEM.() -> Unit = {}
    private var longItemClick: ITEM.() -> Unit = {}

    constructor(items: List<ITEM>,
                layoutResId: Int,
                bindHolder: View.(ITEM) -> Unit,
                itemClick: ITEM.() -> Unit = {},
                longItemClick: ITEM.() -> Unit = {}) : this(items, layoutResId, bindHolder) {
        this.itemClick = itemClick
        this.longItemClick = longItemClick
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (items.isNotEmpty()) {
            holder.itemView.bindHolder(items[position])
        }
    }

    override fun onItemClick(itemView: View, position: Int) {
        if (items.isNotEmpty()) {
            items[position].itemClick()
        }
    }

    override fun onLongItemClick(itemView: View, position: Int) {
        if (items.isNotEmpty()) {
            items[position].longItemClick()
        }
    }
}