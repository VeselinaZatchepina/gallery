package com.github.veselinazatchepina.mygallery.allphotos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.veselinazatchepina.mygallery.data.GalleryRepository
import com.github.veselinazatchepina.mygallery.data.local.GalleryLocalDataSource
import com.github.veselinazatchepina.mygallery.data.remote.GalleryRemoteDataSource
import com.github.veselinazatchepina.mygallery.poko.MyPhoto
import com.github.veselinazatchepina.mygallery.poko.Photo
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File


class AllPhotosViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val galleryDataSource by lazy {
        GalleryRepository.getInstance(GalleryRemoteDataSource.getInstance(),
                GalleryLocalDataSource.getInstance())
    }

    //Live data from remote data source
    var livePhotos = MutableLiveData<List<Photo>>()
    //Live data from local data source (saved photos)
    var liveMyPhotos = MutableLiveData<List<MyPhoto>>()

    /**
     * Method gets all recent photos from remote data source.
     *
     * @param page page number for load from remote data source
     */
    fun getAllPhotos(page: Int = 1) {
        compositeDisposable.add(galleryDataSource.getAllPhotos(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recentPhotos ->
                    livePhotos.value = recentPhotos.photosInfo.photos
                }, { error ->
                    //Set empty list
                    livePhotos.value = RecentPhotos().photosInfo.photos
                    error.printStackTrace()
                }))
    }

    /**
     * Method gets photos from local data source
     *
     * @param rootFile  absolute path to the directory on the primary shared/external storage device
     *                  where the application can place persistent files it owns.
     *                  These files are internal to the applications, and not typically visible to the user as media.
     */
    fun getMyPhotos(rootFile: File) {
        liveMyPhotos.value = galleryDataSource.getMyPhotos(rootFile)
    }

    /**
     * Method delete photo from local storage
     *
     * @param myPhotoPath photo path for delete
     */
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