package com.github.veselinazatchepina.mygallery.poko

import com.google.gson.annotations.SerializedName


data class RecentPhotos(@SerializedName("photos")
                        var photosInfo: PhotosInfo = PhotosInfo(),
                        @SerializedName("stat")
                        var status: String = "")