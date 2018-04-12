package com.github.veselinazatchepina.mygallery.tests

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.BundleMatchers
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.allphotos.AllPhotosMainActivity
import com.github.veselinazatchepina.mygallery.data.local.GalleryLocalDataSource
import com.github.veselinazatchepina.mygallery.enums.PhotoType
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.io.FilenameFilter
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class AllPhotosInstTest {

    @get: Rule
    var rule: IntentsTestRule<AllPhotosMainActivity> =
            IntentsTestRule(AllPhotosMainActivity::class.java)
    private var rootFile: File? = null

    @Before
    fun init() {
        rootFile = rule.activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        saveTestPhotoToLocalStorage()
    }

    private fun saveTestPhotoToLocalStorage() {
        val fileForBitmap = File(rootFile, "android_test.png")
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(fileForBitmap)
            val icon = BitmapFactory.decodeResource(rule.activity.resources, R.drawable.android_test)
            icon.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Test
    fun isDeletePhotoDialogVisibleOnAllPhotosActivityWhenLongClick() {
        onView(ViewMatchers.withText(rule.activity.getString(R.string.tab_title_my_photos)))
                .perform(ViewActions.click())
        onView(allOf(ViewMatchers.withId(R.id.recyclerView),
                withContentDescription(rule.activity.getString(PhotoType.MY_PHOTOS.resource))))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.longClick()))
        Espresso.onView(ViewMatchers.withText(rule.activity.getString(R.string.dialog_delete_photo_title)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isMyPhotosVisibleOnCurrentPhotoActivity() {
        onView(ViewMatchers.withText(rule.activity.getString(R.string.tab_title_my_photos)))
                .perform(ViewActions.click())
        onView(allOf(ViewMatchers.withId(R.id.recyclerView),
                withContentDescription(rule.activity.getString(PhotoType.MY_PHOTOS.resource))))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
        Intents.intended(IntentMatchers.hasExtras(
                BundleMatchers.hasEntry(CoreMatchers.equalTo("current_page_key_intent"), CoreMatchers.equalTo(-1))
        ))
    }

    @Test
    fun isDeletePhotoDialogVisibleOnCurrentPhotoActivityViewPagerItemLongClick() {
        onView(ViewMatchers.withText(rule.activity.getString(R.string.tab_title_my_photos)))
                .perform(ViewActions.click())
        onView(allOf(ViewMatchers.withId(R.id.recyclerView),
                withContentDescription(rule.activity.getString(PhotoType.MY_PHOTOS.resource))))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
        onView(ViewMatchers.withId(R.id.viewPagerCurrentPhoto)).perform(ViewActions.longClick())
        Espresso.onView(ViewMatchers.withText(rule.activity.getString(R.string.dialog_delete_photo_title)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun finish() {
        //Delete test photo
        val imageFilter = FilenameFilter { _, name ->
            name.endsWith("android_test.png")
        }
        val pathArray = rootFile?.list(imageFilter)
        val pathToTestPhoto = "${rootFile?.absolutePath}/${pathArray?.get(0)}"
        File(pathToTestPhoto).delete()
    }
}