package com.github.veselinazatchepina.mygallery.currentphoto.adapters

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.dialogs.DialogDeletePhotoFromCurrentPhotoActivity
import com.github.veselinazatchepina.mygallery.dialogs.SavePhotoDialog
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.current_photo_item.view.*
import kotlinx.android.synthetic.main.error_current_photo.view.*
import java.io.File
import java.lang.Exception


class CurrentPhotoPageAdapter(private val context: FragmentActivity,
                              private val urls: List<String>,
                              private val isMyPhotos: Boolean) : PagerAdapter() {

    companion object {
        private const val DIALOG_SAVE_CURRENT_PHOTO_TAG = "dialog_save_current_photo_tag"
        private const val DIALOG_DELETE_CURRENT_PHOTO_TAG = "dialog_delete_current_photo_tag"
    }

    private val layoutInflater by lazy {
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        if (urls.isEmpty()) {
            context.finish()
        }
        return urls.size
    }

    override fun isViewFromObject(view: View, currentObject: Any) = view == currentObject as LinearLayout

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.current_photo_item, container, false)
        downloadPhoto(urls[position], itemView.currentPhoto, itemView.errorCurrentPhotoText)
        container.addView(itemView)
        defineImageViewLongClickListener(itemView, position)
        return itemView
    }

    private fun defineImageViewLongClickListener(itemView: View, position: Int) {
        itemView.currentPhoto.setOnLongClickListener {
            if (isMyPhotos) {
                DialogDeletePhotoFromCurrentPhotoActivity.newInstance(urls[position])
                        .show(context.supportFragmentManager, DIALOG_DELETE_CURRENT_PHOTO_TAG)
                false
            } else {
                SavePhotoDialog.newInstance(urls[position])
                        .show(context.supportFragmentManager, DIALOG_SAVE_CURRENT_PHOTO_TAG)
                false
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, currentObject: Any) {
        container.removeView(currentObject as LinearLayout)
    }

    fun getCurrentItemPosition(item: String) = when (urls.indexOf(item)) {
        -1 -> 0
        else -> urls.indexOf(item)
    }

    fun getItem(position: Int) = urls[position]

    fun addAll(newUrls: List<String>) {
        (urls as ArrayList<String>).addAll(newUrls)
        notifyDataSetChanged()
    }

    private fun downloadPhoto(url: String, imageView: ImageView, errorView: View?) {
        if (isMyPhotos) {
            Picasso.get()
                    .load(File(url))
        } else {
            Picasso.get()
                    .load(url)
        }.placeholder(R.drawable.empty_image)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        errorView?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        errorView?.visibility = View.VISIBLE
                    }
                })
    }
}
