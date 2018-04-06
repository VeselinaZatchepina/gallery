package com.github.veselinazatchepina.mygallery.allphotos

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.github.veselinazatchepina.mygallery.data.GalleryRepository
import com.github.veselinazatchepina.mygallery.data.remote.GalleryRemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class AllPhotosViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val galleryDataSource by lazy {
        GalleryRepository.getInstance(GalleryRemoteDataSource.getInstance())
    }

    fun getAllPhotos() {
        compositeDisposable.add(galleryDataSource.getAllPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photos ->
                    Log.d("PHOTOS", "${photos.photosInfo.photos.size}")
                }, { error ->
                    Log.d("PHOTOS_ERROR", "${error.printStackTrace()}")
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}