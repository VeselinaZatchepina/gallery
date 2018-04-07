package com.github.veselinazatchepina.mygallery.currentphoto.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.currentphoto.CurrentPhotoViewModel
import kotlinx.android.synthetic.main.current_photo_item.view.*
import kotlinx.android.synthetic.main.error_current_photo.view.*


class CurrentPhotoPageAdapter(private val context: Context,
                              private val urls: List<String>,
                              private val currentPhotoViewModel: CurrentPhotoViewModel) : PagerAdapter() {

    private val layoutInflater by lazy {
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount() = urls.size

    override fun isViewFromObject(view: View, currentObject: Any) = view == currentObject as LinearLayout

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.current_photo_item, container, false)
        currentPhotoViewModel.downloadPhoto(urls[position], itemView.currentPhoto, itemView.errorCurrentPhotoText)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, currentObject: Any) {
        container.removeView(currentObject as LinearLayout)
    }
}