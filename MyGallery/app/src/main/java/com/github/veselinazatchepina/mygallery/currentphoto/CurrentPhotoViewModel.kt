package com.github.veselinazatchepina.mygallery.currentphoto

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.github.veselinazatchepina.mygallery.data.GalleryRepository
import com.github.veselinazatchepina.mygallery.data.local.GalleryLocalDataSource
import com.github.veselinazatchepina.mygallery.data.remote.GalleryRemoteDataSource
import com.github.veselinazatchepina.mygallery.poko.PhotosInfo
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CurrentPhotoViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val galleryDataSource by lazy {
        GalleryRepository.getInstance(GalleryRemoteDataSource.getInstance(),
                GalleryLocalDataSource.getInstance())
    }

    var livePhotosInfo = MutableLiveData<PhotosInfo>()

    fun getAllPhotos(page: Int = 1) {
        compositeDisposable.add(galleryDataSource.getAllPhotos(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recentPhotos ->
                    Log.d("PHOTOS", "OK")
                    livePhotosInfo.value = recentPhotos.photosInfo
                }, { error ->
                    livePhotosInfo.value = RecentPhotos().photosInfo
                    Log.d("PHOTOS_ERROR", "${error.printStackTrace()}")
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}