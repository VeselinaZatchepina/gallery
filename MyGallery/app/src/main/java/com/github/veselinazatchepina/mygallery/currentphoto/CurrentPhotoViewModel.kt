package com.github.veselinazatchepina.mygallery.currentphoto

import android.arch.lifecycle.ViewModel
import android.widget.ImageView
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.data.GalleryRepository
import com.github.veselinazatchepina.mygallery.data.remote.GalleryRemoteDataSource
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable


class CurrentPhotoViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val galleryDataSource by lazy {
        GalleryRepository.getInstance(GalleryRemoteDataSource.getInstance())
    }

    fun downloadPhoto(url: String, imageView: ImageView) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .into(imageView)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}