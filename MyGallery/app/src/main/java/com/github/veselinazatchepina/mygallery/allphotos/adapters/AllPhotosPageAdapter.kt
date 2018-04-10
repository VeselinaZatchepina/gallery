package com.github.veselinazatchepina.mygallery.allphotos.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.github.veselinazatchepina.mygallery.allphotos.fragments.AllPhotosFragment
import com.github.veselinazatchepina.mygallery.allphotos.fragments.MyPhotosFragment

/**
 * PageAdapter for ViewPager in AllPhotosMainActivity
 *
 * @property tabTitles it is list of tab's titles
 */
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