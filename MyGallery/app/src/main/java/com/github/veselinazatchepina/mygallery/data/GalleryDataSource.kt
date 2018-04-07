package com.github.veselinazatchepina.mygallery.data

import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.Flowable


interface GalleryDataSource {

    fun getAllPhotos(page: Int): Flowable<RecentPhotos>
}