package com.github.veselinazatchepina.mygallery.data

import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.Flowable


class GalleryRepository private constructor(val galleryRemoteDataSource: GalleryDataSource) : GalleryDataSource {

    companion object {
        private var INSTANCE: GalleryRepository? = null

        fun getInstance(galleryRemoteDataSource: GalleryDataSource): GalleryRepository {
            if (INSTANCE == null) {
                INSTANCE = GalleryRepository(galleryRemoteDataSource)
            }
            return INSTANCE!!
        }
    }

    override fun getAllPhotos(page: Int): Flowable<RecentPhotos> {
        return galleryRemoteDataSource.getAllPhotos(page)
    }
}