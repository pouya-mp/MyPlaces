package com.pouyaa.myplaces

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_place.*
import java.io.IOException


class AddPlaceActivity : AppCompatActivity() {

    private lateinit var savedImageOnInternalStorage: Uri
    private var selectedPlaceLatitude: Double = 0.0
    private var selectedPlaceLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)
        setSupportActionBar(addPlaceToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addPlaceToolbar.setNavigationOnClickListener { onBackPressed() }


        addDateEditText.setOnClickListener { PickDate(addDateEditText).selectDate(this) }
        addImageTextView.setOnClickListener { showChooseImageDialog() }
        saveButton.setOnClickListener {
            if (checkIsEmptyOnAddPlaceFields()) {
                saveToDatabase()
            }
        }


    }

    private fun showChooseImageDialog() {
        val chooseImageDialog = AlertDialog.Builder(this)
        chooseImageDialog.setTitle(getString(R.string.selectImageAlertDialog))
        val dialogItems = arrayOf(
            getString(R.string.selectPhotoFromLibrary),
            getString(R.string.CapturePhotoFromCamera)
        )
        chooseImageDialog.setItems(dialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        chooseImageDialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)

                        imageOfPlaceImageView.setImageBitmap(selectedImageBitmap)
                        savedImageOnInternalStorage = SaveImageToInternalStorage(
                            IMAGE_DIRECTORY,
                            applicationContext as ContextWrapper
                        ).saveImage(selectedImageBitmap)

                        Log.e("Saved Image : ", "Path :: $savedImageOnInternalStorage")


                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddPlaceActivity,
                            getString(R.string.failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

            } else if (requestCode == CAMERA) {

                val thumbnail: Bitmap = data?.extras?.get("data") as Bitmap
                imageOfPlaceImageView.setImageBitmap(thumbnail)
                savedImageOnInternalStorage = SaveImageToInternalStorage(
                    IMAGE_DIRECTORY,
                    applicationContext as ContextWrapper
                ).saveImage(thumbnail)

                Log.e("Saved Image : ", "Path :: $savedImageOnInternalStorage")
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {


                    if (report != null) {
                        if (report.areAllPermissionsGranted()) {

                            val galleryIntent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )

                            startActivityForResult(galleryIntent, GALLERY)

                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread()
            .check()
    }

    private fun takePhotoFromCamera() {

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null) {
                        if (report.areAllPermissionsGranted()) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread()
            .check()
    }

    private fun showRationalDialogForPermissions() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setMessage(getString(R.string.goToSettingsMessage))
            .setPositiveButton(
                getString(R.string.goToSettings)
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog,
                                                             _ ->
                dialog.dismiss()
            }.show()
    }


    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "MyPlacesImages"
    }

    private fun checkIsEmptyOnAddPlaceFields(): Boolean {
        var result = false
        when {

            addTitleEditText.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.pleaseEnterTitle), Toast.LENGTH_SHORT)
                    .show()
            }
            addDescriptionEditText.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.pleaseEnterDescription), Toast.LENGTH_SHORT)
                    .show()
            }
            addDateEditText.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.pleaseEnterDate), Toast.LENGTH_SHORT)
                    .show()
            }
            addLocationEditText.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.pleaseSelectLocation), Toast.LENGTH_SHORT)
                    .show()
            }
            savedImageOnInternalStorage.toString().isEmpty() -> {
                Toast.makeText(this, getString(R.string.pleaseAddImage), Toast.LENGTH_SHORT).show()
            }
            else -> {
                result = true
            }
        }
        return result
    }

    private fun saveToDatabase() {
        val myPlaceModel = PlaceModel(
            0,
            addTitleEditText.text.toString(),
            savedImageOnInternalStorage.toString(),
            addDescriptionEditText.text.toString(),
            addDateEditText.text.toString(),
            addLocationEditText.text.toString(),
            selectedPlaceLatitude,
            selectedPlaceLongitude
        )


        val dbHandler = DataBaseHandler(this)

        val addPlace = dbHandler.addMyPlace(myPlaceModel)

        if (addPlace > 0) {
            Toast.makeText(this, "Saved Place Successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


}
