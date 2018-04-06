package com.github.veselinazatchepina.mygallery.allphotos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.widget.ImageView
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.data.GalleryRepository
import com.github.veselinazatchepina.mygallery.data.remote.GalleryRemoteDataSource
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class AllPhotosViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val galleryDataSource by lazy {
        GalleryRepository.getInstance(GalleryRemoteDataSource.getInstance())
    }

    var livePhotos = MutableLiveData<RecentPhotos>()

    fun getAllPhotos() {
        compositeDisposable.add(galleryDataSource.getAllPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recentPhotos ->
                    Log.d("PHOTOS", "OK")
                    livePhotos.value = recentPhotos
                }, { error ->
                    Log.d("PHOTOS_ERROR", "${error.printStackTrace()}")
                }))
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