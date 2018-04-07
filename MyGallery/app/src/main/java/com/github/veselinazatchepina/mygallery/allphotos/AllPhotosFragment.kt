package com.github.veselinazatchepina.mygallery.allphotos

import EndlessRecyclerViewScrollListener
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.abstracts.AdapterImpl
import com.github.veselinazatchepina.mygallery.poko.Photo
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.recycler_view_image_item.view.*
import org.jetbrains.anko.support.v4.toast


class AllPhotosFragment : Fragment() {

    private var rootView: View? = null
    private val allPhotosViewModel by lazy {
        ViewModelProviders.of(this).get(AllPhotosViewModel::class.java)
    }
    private lateinit var photosAdapter: AdapterImpl<Photo>

    companion object {
        fun createInstance(): AllPhotosFragment {
            return AllPhotosFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.recycler_view, container, false)
        allPhotosViewModel.getAllPhotos()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPhotosAdapter()
        allPhotosViewModel.livePhotos.observe(this, Observer {
            photosAdapter.addAll(it?.photosInfo?.photos ?: emptyList())
            Log.d("PHOTOS", "${it?.photosInfo?.photos?.size}")
        })
    }

    private fun createPhotosAdapter() {
        photosAdapter = AdapterImpl(arrayListOf<Photo>(),
                R.layout.recycler_view_image_item, {
            if (it.url.isNotEmpty()) {
                allPhotosViewModel.downloadPhoto(it.url, currentImage)
            }
        }, {
            toast("Click!")
        }, {
            toast("LongClick!")
        })
        defineRecyclerView(recyclerView)
    }

    private fun defineRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = photosAdapter
        val layoutManager = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = layoutManager

        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                allPhotosViewModel.getAllPhotos(page)
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }
}