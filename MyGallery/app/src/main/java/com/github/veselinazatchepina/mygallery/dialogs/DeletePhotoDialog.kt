package com.github.veselinazatchepina.mygallery.dialogs

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.github.veselinazatchepina.mygallery.R
import com.github.veselinazatchepina.mygallery.allphotos.AllPhotosViewModel
import kotlinx.android.synthetic.main.dialog_photo.view.*


class DeletePhotoDialog : DialogFragment() {

    private val photoPathForDelete by lazy {
        arguments?.getString(PHOTO_PATH_DIALOG_BUNDLE) ?: ""
    }
    private val allPhotosViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AllPhotosViewModel::class.java)
    }

    companion object {
        private const val PHOTO_PATH_DIALOG_BUNDLE = "photo_path_dialog_bundle"

        fun newInstance(photoPath: String): DeletePhotoDialog {
            val bundle = Bundle()
            bundle.putString(PHOTO_PATH_DIALOG_BUNDLE, photoPath)
            val fragment = DeletePhotoDialog()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_photo,
                null
        )
        defineOkDialogButton(dialogView)
        defineCancelDialogButton(dialogView)
        return defineAlterDialogBuilder(dialogView).create()
    }

    private fun defineOkDialogButton(dialogView: View) {
        dialogView.okButton.setOnClickListener {
            deletePhoto()
            dismiss()
        }
    }

    private fun deletePhoto() {
        allPhotosViewModel.deleteMyPhoto(photoPathForDelete, activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }

    private fun defineCancelDialogButton(dialogView: View) {
        dialogView.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun defineAlterDialogBuilder(dialogView: View): AlertDialog.Builder {
        return AlertDialog.Builder(activity!!).apply {
            setView(dialogView)
            setTitle(getString(R.string.dialog_delete_photo_title))
            setCancelable(false)
        }
    }
}