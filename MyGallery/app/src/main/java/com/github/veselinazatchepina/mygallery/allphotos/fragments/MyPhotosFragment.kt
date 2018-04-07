package com.github.veselinazatchepina.mygallery.allphotos.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.allphotos.AllPhotosViewModel


class MyPhotosFragment : Fragment() {

    private val allPhotosViewModel by lazy {
        ViewModelProviders.of(this).get(AllPhotosViewModel::class.java)
    }

    companion object {
        fun createInstance(): MyPhotosFragment {
            return MyPhotosFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}