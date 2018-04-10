package com.github.veselinazatchepina.mygallery.data.remote

import com.github.veselinazatchepina.mygallery.data.GalleryDataSource
import com.github.veselinazatchepina.mygallery.poko.MyPhoto
import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Flowable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class GalleryRemoteDataSource private constructor() : GalleryDataSource {

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    companion object {
        private const val BASE_URL = "https://api.flickr.com/services/rest/"

        private var INSTANCE: GalleryRemoteDataSource? = null

        fun getInstance(): GalleryRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = GalleryRemoteDataSource()
            }
            return INSTANCE!!
        }
    }

    override fun getAllPhotos(page: Int): Flowable<RecentPhotos> {
        return retrofit.create(FlickrAPI::class.java).getAllPhotos(page)
    }

    override fun getMyPhotos(rootFile: File): List<MyPhoto> {
        TODO("not implemented because we don't need it")
    }
}