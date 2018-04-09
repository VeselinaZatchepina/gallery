package com.github.veselinazatchepina.mygallery.data

import com.github.veselinazatchepina.mygallery.poko.MyPhoto
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.Flowable
import java.io.File


interface GalleryDataSource {

    fun getAllPhotos(page: Int): Flowable<RecentPhotos>

    fun getMyPhotos(rootFile: File): List<MyPhoto>
}