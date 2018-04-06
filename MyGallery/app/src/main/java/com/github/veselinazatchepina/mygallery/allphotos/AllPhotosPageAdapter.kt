package com.github.veselinazatchepina.mygallery.allphotos

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class AllPhotosPageAdapter(fm: FragmentManager,
                           private val tabTitles: List<String>) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AllPhotosFragment.createInstance()
            else -> MyPhotosFragment.createInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }
}