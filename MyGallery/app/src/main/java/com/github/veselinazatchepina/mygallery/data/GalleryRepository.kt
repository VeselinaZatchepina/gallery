package com.github.veselinazatchepina.mygallery.data

import com.github.veselinazatchepina.mygallery.poko.MyPhoto
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.Flowable
import java.io.File


class GalleryRepository private constructor(private val galleryRemoteDataSource: GalleryDataSource,
                                            private val galleryLocalDataSource: GalleryDataSource) : GalleryDataSource {

    companion object {
        private var INSTANCE: GalleryRepository? = null

        fun getInstance(galleryRemoteDataSource: GalleryDataSource,
                        galleryLocalDataSource: GalleryDataSource): GalleryRepository {
            if (INSTANCE == null) {
                INSTANCE = GalleryRepository(galleryRemoteDataSource, galleryLocalDataSource)
            }
            return INSTANCE!!
        }
    }

    override fun getAllPhotos(page: Int): Flowable<RecentPhotos> {
        return galleryRemoteDataSource.getAllPhotos(page)
    }

    override fun getMyPhotos(rootFile: File): List<MyPhoto> {
        return galleryLocalDataSource.getMyPhotos(rootFile)
    }
}