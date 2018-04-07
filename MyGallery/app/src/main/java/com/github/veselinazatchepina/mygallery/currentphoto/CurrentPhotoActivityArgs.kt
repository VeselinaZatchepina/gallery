package com.github.veselinazatchepina.mygallery.currentphoto

import android.content.Context
import android.content.Intent
import com.github.veselinazatchepina.mygallery.ActivityArgs


data class CurrentPhotoActivityArgs(val photoUrl: String, val urls: ArrayList<String>) : ActivityArgs {

    companion object {
        private const val PHOTO_URL_KEY_INTENT = "photo_url_key_intent"
        private const val URLS_KEY_INTENT = "urls_key_intent"

        fun deserializeFrom(intent: Intent): CurrentPhotoActivityArgs {
            return CurrentPhotoActivityArgs(intent.getStringExtra(PHOTO_URL_KEY_INTENT),
                    intent.getStringArrayListExtra(URLS_KEY_INTENT))
        }
    }

    override fun intent(activity: Context): Intent = Intent(
            activity, CurrentPhotoActivity::class.java
    ).apply {
        putExtra(PHOTO_URL_KEY_INTENT, photoUrl)
        putExtra(URLS_KEY_INTENT, urls)
    }
}