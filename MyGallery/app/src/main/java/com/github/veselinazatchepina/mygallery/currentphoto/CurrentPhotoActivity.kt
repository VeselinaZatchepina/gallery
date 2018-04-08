package com.github.veselinazatchepina.mygallery.currentphoto

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.currentphoto.fragments.CurrentPhotoFragment
import com.github.veselinazatchepina.mygallery.setFirstVowelColor
import kotlinx.android.synthetic.main.activity_current_photo.*


class CurrentPhotoActivity : AppCompatActivity() {

    private val args by lazy {
        CurrentPhotoActivityArgs.deserializeFrom(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_photo)
        defineToolbar()
        defineTitle()
        defineFragment()
    }

    private fun defineToolbar() {
        setSupportActionBar(currentPhotoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun defineTitle() {
        if (title != null) {
            title = resources.getString(R.string.current_photo_title).setFirstVowelColor(this)
        }
    }

    private fun defineFragment() {
        var currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment == null) {
            currentFragment = CurrentPhotoFragment.createInstance(args.photoUrl, args.urls, args.page)
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, currentFragment)
                    .commit()
        }
    }
}