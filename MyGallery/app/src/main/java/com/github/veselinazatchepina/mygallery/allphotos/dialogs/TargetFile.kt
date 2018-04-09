package com.github.veselinazatchepina.mygallery.allphotos.dialogs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class TargetFile(private val context: Context,
                 private val imageDir: String) : Target {

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        context.toast("Please try to save again!")
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        Thread(Runnable {
            val fileForBitmap = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    imageDir)
            var fileOutputStream: FileOutputStream? = null
            try {
                fileOutputStream = FileOutputStream(fileForBitmap)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    fileOutputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }
}