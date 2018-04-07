package com.github.veselinazatchepina.mygallery.data.remote

import com.github.veselinazatchepina.mygallery.poko.RecentPhotos
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query


interface FlickrAPI {

    companion object {
        /*
        https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=47842f477964ef1e88999c4772ef3b60&extras=url_s&format=json&nojsoncallback=1/
         */
        private const val API_KEY: String = "47842f477964ef1e88999c4772ef3b60"
        private const val GET_RECENT_QUERY: String = "flickr.photos.getRecent"
        private const val EXTRAS: String = "url_s"
        private const val FORMAT: String = "json"
    }

    @GET("?method=$GET_RECENT_QUERY&api_key=$API_KEY&extras=$EXTRAS&format=$FORMAT&nojsoncallback=1")
    fun getAllPhotos(@Query("page") page: Int): Flowable<RecentPhotos>

}