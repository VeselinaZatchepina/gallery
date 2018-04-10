package com.github.veselinazatchepina.mygallery.data.local

import com.github.veselinazatchepina.mygallery.data.GalleryDataSource
import com.github.veselinazatchepina.mygallery.poko.MyPhoto
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.Flowable
import java.io.File
import java.io.FilenameFilter


class GalleryLocalDataSource private constructor() : GalleryDataSource {

    companion object {
        private var INSTANCE: GalleryLocalDataSource? = null

        fun getInstance(): GalleryLocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = GalleryLocalDataSource()
            }
            return INSTANCE!!
        }
    }

    override fun getAllPhotos(page: Int): Flowable<RecentPhotos> {
        TODO("not implemented because we don't need it")
    }

    override fun getMyPhotos(rootFile: File): List<MyPhoto> {
        val myPhotos = arrayListOf<MyPhoto>()
        val imageFilter = FilenameFilter { dir, name ->
            name.endsWith(".png")
        }
        val pathArray = rootFile.list(imageFilter)
        pathArray.mapTo(myPhotos) { MyPhoto("${rootFile.absolutePath}/$it") }
        return myPhotos
    }

}