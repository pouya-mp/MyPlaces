package com.pouyaa.myplaces

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_place.*
import java.text.SimpleDateFormat
import java.util.*

class AddPlaceActivity : AppCompatActivity() {

//    private var cal = Calendar.getInstance()
//    private lateinit var dateSetListener : DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)
        setSupportActionBar(addPlaceToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addPlaceToolbar.setNavigationOnClickListener { onBackPressed() }


        addDateEditText.setOnClickListener {

            PickDate(addDateEditText).selectDate(this)

        }


    }

}
