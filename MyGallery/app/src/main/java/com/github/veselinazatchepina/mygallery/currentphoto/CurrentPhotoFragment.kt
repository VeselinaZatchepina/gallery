package com.github.veselinazatchepina.mygallery.currentphoto

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.mygallery.R
import kotlinx.android.synthetic.main.error_current_photo.*
import kotlinx.android.synthetic.main.fragment_current_photo.*


class CurrentPhotoFragment : Fragment() {

    private var rootView: View? = null
    private val currentPhotoViewModel by lazy {
        ViewModelProviders.of(this).get(CurrentPhotoViewModel::class.java)
    }
    private val photoUrl by lazy {
        arguments?.getString(PHOTO_URL_KEY_BUNDLE) ?: ""
    }

    companion object {
        private const val PHOTO_URL_KEY_BUNDLE = "photo_url_key_bundle"

        fun createInstance(photoUrl: String): CurrentPhotoFragment {
            val bundle = Bundle()
            bundle.putString(PHOTO_URL_KEY_BUNDLE, photoUrl)
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
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentPhotoViewModel.downloadPhoto(photoUrl, currentPhoto, errorCurrentPhotoText)
    }
}