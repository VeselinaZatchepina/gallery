package com.github.veselinazatchepina.mygallery

import android.content.Intent
import com.github.veselinazatchepina.mygallery.currentphoto.CurrentPhotoActivity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.fragment_current_photo.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class AllPhotosTest {

    companion object {
        private const val PHOTO_URL_KEY_INTENT = "photo_url_key_intent"
        private const val URLS_KEY_INTENT = "urls_key_intent"
    }

    private lateinit var activity: CurrentPhotoActivity
    private lateinit var activityController: ActivityController<CurrentPhotoActivity>

    @Before
    fun init() {
        val intent = createIntent()
        activity = Robolectric.buildActivity(CurrentPhotoActivity::class.java, intent)
                .get()
        activityController = Robolectric.buildActivity(CurrentPhotoActivity::class.java, intent)
                .create()
    }

    private fun createIntent() = Intent().apply {
        putExtra(PHOTO_URL_KEY_INTENT, "")
        putExtra(URLS_KEY_INTENT, arrayListOf<String>())
    }

    @Test
    fun isActivityNotNull() {
        assertNotNull(activity)
    }

    @Test
    fun isFragmentExists() {
        val currentActivity = activityController.get()
        assertNotNull(currentActivity.supportFragmentManager.findFragmentById(R.id.container))
    }

    @Test
    fun isViewPagerVisible() {
        val currentActivity = activityController.start().resume().get()
        assertNotNull(currentActivity.viewPagerCurrentPhoto)
    }

    @Test
    fun isCurrentPhotoActivityTitleCorrect() {
        val currentActivity = activityController.start().resume().get()
        assertEquals(currentActivity.title.toString(), currentActivity.resources.getString(R.string.current_photo_title))
    }

    @Test
    fun isMenuVisible() {
        val currentActivity = activityController.start().resume().get()
        assertEquals(currentActivity.supportFragmentManager.findFragmentById(R.id.container).hasOptionsMenu(), true)
    }
}