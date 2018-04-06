package com.github.veselinazatchepina.mygallery.poko

import com.google.gson.annotations.SerializedName


data class RecentPhotos(var photos: PhotosInfo = PhotosInfo(),
                        @SerializedName("stat")
                        var status: String = "")