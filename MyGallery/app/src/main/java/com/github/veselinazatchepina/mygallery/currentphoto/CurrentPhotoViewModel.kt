package com.github.veselinazatchepina.mygallery.currentphoto

import android.arch.lifecycle.ViewModel
import android.view.View
import android.widget.ImageView
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.data.GalleryRepository
import com.github.veselinazatchepina.mygallery.data.remote.GalleryRemoteDataSource
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception


class CurrentPhotoViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val galleryDataSource by lazy {
        GalleryRepository.getInstance(GalleryRemoteDataSource.getInstance())
    }

    fun downloadPhoto(url: String, imageView: ImageView, errorView: View) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.empty_image)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        errorView.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        errorView.visibility = View.VISIBLE
                    }
                })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}