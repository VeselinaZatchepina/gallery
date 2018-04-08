package com.github.veselinazatchepina.mygallery.allphotos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.github.veselinazatchepina.mygallery.data.GalleryRepository
import com.github.veselinazatchepina.mygallery.data.remote.GalleryRemoteDataSource
import com.github.veselinazatchepina.mygallery.poko.Photo
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class AllPhotosViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val galleryDataSource by lazy {
        GalleryRepository.getInstance(GalleryRemoteDataSource.getInstance())
    }

    var livePhotos = MutableLiveData<List<Photo>>()

    fun getAllPhotos(page: Int = 1) {
        compositeDisposable.add(galleryDataSource.getAllPhotos(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recentPhotos ->
                    Log.d("PHOTOS", "OK")
                    livePhotos.value = recentPhotos.photosInfo.photos
                }, { error ->
                    livePhotos.value = RecentPhotos().photosInfo.photos
                    Log.d("PHOTOS_ERROR", "${error.printStackTrace()}")
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}