package com.github.veselinazatchepina.mygallery.allphotos

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.allphotos.adapters.AllPhotosPageAdapter
import com.github.veselinazatchepina.mygallery.enums.PhotoType
import com.github.veselinazatchepina.mygallery.setFirstVowelColor
import kotlinx.android.synthetic.main.activity_all_photos_main.*

class AllPhotosMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_photos_main)
        defineTabLayout()
        defineToolbar()
        defineTitle()
    }

    private fun defineTabLayout() {
        val pageAdapter = AllPhotosPageAdapter(supportFragmentManager,
                arrayListOf(getString(PhotoType.ALL_PHOTOS.resource),
                        getString(PhotoType.MY_PHOTOS.resource)))
        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun defineToolbar() {
        setSupportActionBar(tabToolbar)
    }

    private fun defineTitle() {
        if (title != null) {
            title = resources.getString(R.string.all_photo_title).setFirstVowelColor(this)
        }
    }
}
