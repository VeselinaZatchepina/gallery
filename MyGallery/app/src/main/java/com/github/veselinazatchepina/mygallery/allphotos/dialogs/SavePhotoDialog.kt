package com.github.veselinazatchepina.mygallery.allphotos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.github.veselinazatchepina.mygallery.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_save_photo.view.*


class SavePhotoDialog : DialogFragment() {

    private val photoUrlForSave by lazy {
        arguments?.getString(PHOTO_URL_DIALOG_BUNDLE) ?: ""
    }

    companion object {
        private const val PHOTO_URL_DIALOG_BUNDLE = "photo_url_dialog_bundle"

        fun newInstance(photoUrl: String): SavePhotoDialog {
            val bundle = Bundle()
            bundle.putString(PHOTO_URL_DIALOG_BUNDLE, photoUrl)
            val fragment = SavePhotoDialog()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_save_photo,
                null
        )
        defineSaveDialogButton(dialogView)
        defineCancelDialogButton(dialogView)
        return defineAlterDialogBuilder(dialogView).create()
    }

    private fun defineSaveDialogButton(dialogView: View) {
        dialogView.savePhotoButton.setOnClickListener {
            savePhoto()
            dismiss()
        }
    }

    private fun savePhoto() {
        Picasso.get()
                .load(photoUrlForSave)
                .into(TargetFile(activity!!,"${System.currentTimeMillis()}.png"))
    }

    private fun defineCancelDialogButton(dialogView: View) {
        dialogView.cancelPhotoButton.setOnClickListener {
            dismiss()
        }
    }

    private fun defineAlterDialogBuilder(dialogView: View): AlertDialog.Builder {
        return AlertDialog.Builder(activity!!).apply {
            setView(dialogView)
            setTitle(getString(R.string.dialog_save_photo_title))
            setCancelable(false)
        }
    }
}