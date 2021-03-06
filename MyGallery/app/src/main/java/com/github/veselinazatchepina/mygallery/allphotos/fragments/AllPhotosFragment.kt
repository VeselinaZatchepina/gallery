package com.github.veselinazatchepina.mygallery.allphotos.fragments

import EndlessRecyclerViewScrollListener
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.abstracts.AdapterImpl
import com.github.veselinazatchepina.mygallery.allphotos.AllPhotosViewModel
import com.github.veselinazatchepina.mygallery.dialogs.SavePhotoDialog
import com.github.veselinazatchepina.mygallery.enums.PhotoType
import com.github.veselinazatchepina.mygallery.observeData
import com.github.veselinazatchepina.mygallery.poko.Photo
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.recycler_view_empty.*
import kotlinx.android.synthetic.main.recycler_view_image_item.view.*
import java.lang.Exception


class AllPhotosFragment : Fragment() {

    private var rootView: View? = null
    private val allPhotosViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AllPhotosViewModel::class.java)
    }
    private lateinit var allPhotosAdapter: AdapterImpl<Photo>
    private lateinit var recyclerScrollListener: EndlessRecyclerViewScrollListener
    //Page number for load from remote data source. It uses to launch CurrentPhotoActivity.
    private var currentPageForDownload = 1
    private var activityCallback: AllPhotosFragment.AllPhotosListener? = null

    companion object {
        private const val SAVE_PHOTO_DIALOG_TAG = "save_photo_dialog_key"

        fun createInstance(): AllPhotosFragment {
            return AllPhotosFragment()
        }
    }

    interface AllPhotosListener {
        fun launchCurrentPhotoActivity(url: String, allUrls: List<String>, currentPage: Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as AllPhotosListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement AllPhotosListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.recycler_view, container, false)
        allPhotosViewModel.getAllPhotos()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPhotosAdapter()
        defineSwipeRefreshLayout()
        allPhotosViewModel.livePhotos.observe(this, Observer {
            if (it != null) {
                //If we want to load photos again by swipe
                if (swipeRefreshLayout.isRefreshing) {
                    allPhotosAdapter.update(it.filter { it.url.isNotEmpty() })
                    swipeRefreshLayout.isRefreshing = false
                } else {
                    //If we scroll RecyclerView
                    allPhotosAdapter.addAll(it.filter { it.url.isNotEmpty() })
                }
            }
        })
    }

    /**
     * Fun creates adapter for RecyclerView
     */
    private fun createPhotosAdapter() {
        allPhotosAdapter = AdapterImpl(arrayListOf<Photo>(),
                R.layout.recycler_view_image_item, {
            downloadPhoto(it.url, currentImage)
        }, {
            activityCallback?.launchCurrentPhotoActivity(this.url,
                    allPhotosAdapter.getAdapterItems().map { it.url } as ArrayList<String>,
                    currentPageForDownload)
        }, {
            SavePhotoDialog.newInstance(url).show(activity!!.supportFragmentManager, SAVE_PHOTO_DIALOG_TAG)
        })
        defineAdapterDataObserver()
        defineRecyclerView()
    }

    private fun defineSwipeRefreshLayout() {
        setColorsToSwipe()
        swipeRefreshLayout.setOnRefreshListener {
            recyclerScrollListener.resetState()
            allPhotosViewModel.getAllPhotos()
        }
    }

    private fun setColorsToSwipe() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.gradient_start),
                ContextCompat.getColor(activity!!, R.color.gradient_end),
                ContextCompat.getColor(activity!!, R.color.colorAccent))
    }


    /**
     * Fun helps to observe adapter's data. If it has empty data we set empty view.
     */
    private fun defineAdapterDataObserver() {
        emptyText.text = resources.getString(R.string.error_view_text)
        allPhotosAdapter.observeData(emptyText)
    }

    private fun defineRecyclerView() {
        recyclerView.contentDescription = activity?.resources?.getString(PhotoType.ALL_PHOTOS.resource)
        recyclerView.adapter = allPhotosAdapter
        val layoutManager = GridLayoutManager(activity, activity!!.resources.getInteger(R.integer.grid_span_count))
        recyclerView.layoutManager = layoutManager
        defineRecyclerViewScrollListener(layoutManager)
    }

    /**
     * Fun defines ScrollListener for loading new data when scrolling
     */
    private fun defineRecyclerViewScrollListener(gridlayoutManager: GridLayoutManager) {
        recyclerScrollListener = object : EndlessRecyclerViewScrollListener(gridlayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                currentPageForDownload = page
                allPhotosViewModel.getAllPhotos(page)
            }
        }
        recyclerView.addOnScrollListener(recyclerScrollListener)
    }

    /**
     * Fun downloads photo by url to imageView
     *
     * @param url photo's url
     * @param imageView view for photo
     */
    private fun downloadPhoto(url: String, imageView: ImageView) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.empty_image)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        emptyText?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        //Set error text
                        emptyText?.text = resources.getString(R.string.error_view_text)
                        emptyText?.visibility = View.VISIBLE
                    }
                })
    }
}