package com.github.veselinazatchepina.mygallery.poko

import com.google.gson.annotations.SerializedName


data class PhotosInfo(var page: Int = -1,
                      var pages: Int = -1,
                      @SerializedName("perpage")
                      var perPage: Int = -1,
                      var total: Int = -1,
                      @SerializedName("photo")
                      var photos: List<Photo> = emptyList())