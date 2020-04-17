package com.pouyaa.myplaces

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class SaveImageToInternalStorage(imageDirectory: String, applicationContext: ContextWrapper) {

    private val wrapper = ContextWrapper(applicationContext)
    private var file = wrapper.getDir(imageDirectory, Context.MODE_PRIVATE)

     fun saveImage(bitmap: Bitmap): Uri {
        file = File(file, "${UUID.randomUUID()}.jpg")


        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }


}