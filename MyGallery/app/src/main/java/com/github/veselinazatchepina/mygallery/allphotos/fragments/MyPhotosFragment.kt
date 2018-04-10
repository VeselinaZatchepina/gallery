package com.github.veselinazatchepina.mygallery.allphotos.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.abstracts.AdapterImpl
import com.github.veselinazatchepina.mygallery.allphotos.AllPhotosViewModel
import com.github.veselinazatchepina.mygallery.currentphoto.CurrentPhotoActivityArgs
import com.github.veselinazatchepina.mygallery.dialogs.DeletePhotoFromListDialog
import com.github.veselinazatchepina.mygallery.observeData
import com.github.veselinazatchepina.mygallery.poko.MyPhoto
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.recycler_view_empty.*
import kotlinx.android.synthetic.main.recycler_view_image_item.view.*
import java.io.File
import java.lang.Exception


class MyPhotosFragment : Fragment() {

    private lateinit var myPhotosAdapter: AdapterImpl<MyPhoto>

    private val allPhotosViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AllPhotosViewModel::class.java)
    }

    companion object {
        private const val DELETE_PHOTO_DIALOG_TAG = "delete_photo_dialog_key"

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
        createMyPhotosAdapter()
        defineSwipeRefreshLayout()
        allPhotosViewModel.getMyPhotos(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        allPhotosViewModel.liveMyPhotos.observe(this, Observer {
            Log.d("UPDATE", "update1")
            if (it != null) {
                Log.d("UPDATE", "update")
                myPhotosAdapter.update(it)
                if (swipeRefreshLayout.isRefreshing) {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        allPhotosViewModel.getMyPhotos(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (this.isVisible) {
            allPhotosViewModel.getMyPhotos(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        }
    }

    private fun defineSwipeRefreshLayout() {
        setColorsToSwipe()
        swipeRefreshLayout.setOnRefreshListener {
            allPhotosViewModel.getMyPhotos(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        }
    }

    private fun setColorsToSwipe() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.gradient_start),
                ContextCompat.getColor(activity!!, R.color.gradient_end),
                ContextCompat.getColor(activity!!, R.color.colorAccent))
    }

    private fun createMyPhotosAdapter() {
        myPhotosAdapter = AdapterImpl(arrayListOf<MyPhoto>(),
                R.layout.recycler_view_image_item, {
            downloadPhoto(it.path, currentImage)
        }, {
            CurrentPhotoActivityArgs(path, allPhotosViewModel.liveMyPhotos
                    .value
                    ?.map { it.path } as ArrayList<String>)
                    .launch(activity!!)
        }, {
            DeletePhotoFromListDialog.newInstance(this.path).show(activity!!.supportFragmentManager, DELETE_PHOTO_DIALOG_TAG)
        })
        defineAdapterDataObserver()
        defineRecyclerView()
    }

    private fun downloadPhoto(url: String, imageView: ImageView) {
        Picasso.get()
                .load(File(url))
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        emptyText?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        emptyText?.text = getString(R.string.error_view_text)
                        emptyText?.visibility = View.VISIBLE
                    }
                })
    }

    private fun defineAdapterDataObserver() {
        emptyText.text = resources.getString(R.string.empty_view_text)
        myPhotosAdapter.observeData(emptyText)
    }

    private fun defineRecyclerView() {
        recyclerView.adapter = myPhotosAdapter
        val layoutManager = GridLayoutManager(activity, activity!!.resources.getInteger(R.integer.grid_span_count))
        recyclerView.layoutManager = layoutManager
    }


}