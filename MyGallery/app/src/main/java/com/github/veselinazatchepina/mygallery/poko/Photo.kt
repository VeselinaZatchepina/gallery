package com.github.veselinazatchepina.mygallery.poko

import com.google.gson.annotations.SerializedName

/**
 * Photo from remote storage
 */
data class Photo(var id: String = "",
                 var title: String = "",
                 @SerializedName("url_s")
                 var url: String = "",
                 @SerializedName("height_s")
                 var height: String = "",
                 @SerializedName("width_s")
                 var width: String = "")

