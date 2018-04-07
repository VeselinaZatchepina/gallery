package com.github.veselinazatchepina.mygallery.currentphoto

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.data.GalleryRepository
import com.github.veselinazatchepina.mygallery.data.remote.GalleryRemoteDataSource
import com.github.veselinazatchepina.mygallery.poko.PhotosInfo
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception


class CurrentPhotoViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val galleryDataSource by lazy {
        GalleryRepository.getInstance(GalleryRemoteDataSource.getInstance())
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