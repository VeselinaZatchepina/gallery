package com.github.veselinazatchepina.mygallery.currentphoto.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.widget.ImageView
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.currentphoto.CurrentPhotoViewModel
import com.github.veselinazatchepina.mygallery.currentphoto.adapters.CurrentPhotoPageAdapter
import kotlinx.android.synthetic.main.current_photo_item.*
import kotlinx.android.synthetic.main.fragment_current_photo.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CurrentPhotoFragment : Fragment() {

    private var rootView: View? = null
    private val currentPhotoViewModel by lazy {
        ViewModelProviders.of(activity!!).get(CurrentPhotoViewModel::class.java)
    }
    private val photoUrl by lazy {
        arguments?.getString(PHOTO_URL_KEY_BUNDLE) ?: ""
    }
    private val urls by lazy {
        arguments?.getStringArrayList(URLS_KEY_BUNDLE) ?: emptyList<String>()
    }
    private val currentPage by lazy {
        arguments?.getInt(CURRENT_PAGE_KEY_BUNDLE) ?: -1
    }
    private var pageNumberForDownload = 1
    private var isMyPhotos = false
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
        pageNumberForDownload = currentPage
        isMyPhotos = currentPage == -1
        defineViewPager()
        if (isMyPhotos) {
            currentPhotoViewModel.getMyPhotos(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            currentPhotoViewModel.liveMyPhotos.observe(this, Observer {
                viewPagerPhotoAdapter = CurrentPhotoPageAdapter(activity!!, it?.map { it.path } ?: emptyList(), isMyPhotos)
                viewPagerCurrentPhoto.adapter = viewPagerPhotoAdapter
            })
        }
        Log.d("ISMY", "MY $isMyPhotos")
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.current_photo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_share -> sharePhoto()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sharePhoto() {
        val currentBitmapUri = getLocalBitmapUri(currentPhoto)
        if (currentBitmapUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, currentBitmapUri)
            shareIntent.type = "image/*"
            startActivity(Intent.createChooser(shareIntent, "Share Image"))
        }
        File(currentBitmapUri?.path).delete()
    }

    private fun getLocalBitmapUri(imageView: ImageView): Uri? {
        val currentBitmap = getBitmap(imageView)
        var currentBitmapUri: Uri? = null
        try {
            currentBitmapUri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(activity!!,
                        "com.github.veselinazatchepina.mygallery",
                        getFile(currentBitmap))
            } else {
                Uri.fromFile(getFile(currentBitmap))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return currentBitmapUri
    }

    private fun getFile(currentBitmap: Bitmap?) = if (isMyPhotos) {File(viewPagerPhotoAdapter.getItem(viewPagerCurrentPhoto.currentItem))}
    else {getFileWithBitmap(currentBitmap)}

    private fun getFileWithBitmap(currentBitmap: Bitmap?): File {
        val fileForBitmap = File(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png")
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(fileForBitmap)
            currentBitmap?.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return fileForBitmap
    }

    private fun getBitmap(currentImageView: ImageView): Bitmap? {
        val currentDrawable = currentImageView.drawable
        return if (currentDrawable is BitmapDrawable) {
            currentDrawable.bitmap
        } else {
            null
        }
    }

    private fun defineViewPager() {
        viewPagerPhotoAdapter = CurrentPhotoPageAdapter(activity!!, urls, isMyPhotos)
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
                if (!isMyPhotos) {
                    if (position == urls.size - 1) {
                        currentPhotoViewModel.getAllPhotos(++pageNumberForDownload)
                        currentPhotoViewModel.livePhotosInfo.observe(this@CurrentPhotoFragment, Observer {
                            viewPagerPhotoAdapter.addAll(it?.photos?.map { it.url } ?: emptyList())
                        })
                        Log.d("POSITION", "URLS_SIZE")
                    }
                    Log.d("POSITION", "$position")
                }
            }
        })
    }
}