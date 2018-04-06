package com.github.veselinazatchepina.mygallery

import android.content.Context
import android.content.Intent


interface ActivityArgs {
    /**
     * @return returns an intent that can be used to launch this activity
     */
    fun intent(activity: Context): Intent

    /**
     * Launches the activity given your activity context
     */
    fun launch(activity: Context) = activity.startActivity(intent(activity))
}