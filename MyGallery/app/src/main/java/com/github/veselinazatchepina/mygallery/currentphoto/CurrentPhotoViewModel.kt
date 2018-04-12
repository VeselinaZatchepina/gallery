package com.github.veselinazatchepina.mygallery.currentphoto

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.veselinazatchepina.mygallery.data.GalleryRepository
import com.github.veselinazatchepina.mygallery.data.local.GalleryLocalDataSource
import com.github.veselinazatchepina.mygallery.data.remote.GalleryRemoteDataSource
import com.github.veselinazatchepina.mygallery.poko.MyPhoto
import com.github.veselinazatchepina.mygallery.poko.PhotosInfo
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File


class CurrentPhotoViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val galleryDataSource by lazy {
        GalleryRepository.getInstance(GalleryRemoteDataSource.getInstance(),
                GalleryLocalDataSource.getInstance())
    }

    //Live data from remote data source
    var livePhotosInfo = MutableLiveData<PhotosInfo>()
    //Live data from local data source (saved photos)
    var liveMyPhotos = MutableLiveData<List<MyPhoto>>()

    fun getAllPhotos(page: Int = 1) {
        compositeDisposable.add(galleryDataSource.getAllPhotos(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recentPhotos ->
                    livePhotosInfo.value = recentPhotos.photosInfo
                }, { error ->
                    livePhotosInfo.value = RecentPhotos().photosInfo
                    error.printStackTrace()
                }))
    }

    private fun getMyPhotos(rootFile: File) {
        liveMyPhotos.value = galleryDataSource.getMyPhotos(rootFile)
    }

    fun deleteMyPhoto(myPhotoPath: String, rootFile: File) {
        val photoFile = File(myPhotoPath)
        photoFile.delete()
        getMyPhotos(rootFile)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}