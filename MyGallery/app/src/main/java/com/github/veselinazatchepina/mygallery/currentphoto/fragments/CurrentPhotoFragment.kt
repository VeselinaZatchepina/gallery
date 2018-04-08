package com.github.veselinazatchepina.mygallery.currentphoto.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.currentphoto.CurrentPhotoViewModel
import com.github.veselinazatchepina.mygallery.currentphoto.adapters.CurrentPhotoPageAdapter
import kotlinx.android.synthetic.main.current_photo_item.*
import kotlinx.android.synthetic.main.fragment_current_photo.*
import org.jetbrains.anko.support.v4.toast


class CurrentPhotoFragment : Fragment() {

    private var rootView: View? = null
    private val currentPhotoViewModel by lazy {
        ViewModelProviders.of(this).get(CurrentPhotoViewModel::class.java)
    }
    private val photoUrl by lazy {
        arguments?.getString(PHOTO_URL_KEY_BUNDLE) ?: ""
    }
    private val urls by lazy {
        arguments?.getStringArrayList(URLS_KEY_BUNDLE) ?: emptyList<String>()
    }
    private val currentPage by lazy {
        arguments?.getInt(CURRENT_PAGE_KEY_BUNDLE)
    }
    private var pageNumberForDownload = currentPage ?: 1
    private lateinit var viewPagerPhotoAdapter: CurrentPhotoPageAdapter

    companion object {
        private const val PHOTO_URL_KEY_BUNDLE = "photo_url_key_bundle"
        private const val URLS_KEY_BUNDLE = "urls_key_bundle"
        private const val CURRENT_PAGE_KEY_BUNDLE = "current_page_key_bundle"

        fun createInstance(photoUrl: String,
                           urls: ArrayList<String>,
                           page: Int): CurrentPhotoFragment {
            val bundle = Bundle()
            bundle.putString(PHOTO_URL_KEY_BUNDLE, photoUrl)
            bundle.putStringArrayList(URLS_KEY_BUNDLE, urls)
            bundle.putInt(CURRENT_PAGE_KEY_BUNDLE, page)
            val fragment = CurrentPhotoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_current_photo, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineViewPager()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.current_photo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_share -> toast("Share")
            R.id.menu_item_rotate -> rotateImage()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun rotateImage() {
        val currentBitmap = (currentPhoto.drawable as BitmapDrawable).bitmap
        val matrix = Matrix()
        matrix.postRotate(90F)
        val rotatedBitmap = Bitmap.createBitmap(currentBitmap,
                0,
                0,
                currentBitmap.width,
                currentBitmap.height,
                matrix,
                true)
        currentPhoto.setImageBitmap(rotatedBitmap)
    }

    private fun defineViewPager() {
        viewPagerPhotoAdapter = CurrentPhotoPageAdapter(activity!!, urls)
        viewPagerCurrentPhoto.adapter = viewPagerPhotoAdapter
        viewPagerCurrentPhoto.currentItem = viewPagerPhotoAdapter.getCurrentItemPosition(photoUrl)
        defineViewPagerPageListener()
    }

    private fun defineViewPagerPageListener() {
        viewPagerCurrentPhoto.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == urls.size - 1) {
                    currentPhotoViewModel.getAllPhotos(++pageNumberForDownload)
                    currentPhotoViewModel.livePhotosInfo.observe(this@CurrentPhotoFragment, Observer {
                        viewPagerPhotoAdapter.addAll(it?.photos?.map { it.url } ?: emptyList())
                    })
                    Log.d("POSITION", "URLS_SIZE")
                }
                Log.d("POSITION", "$position")
            }
        })
    }
}