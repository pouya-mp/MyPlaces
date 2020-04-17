package com.pouyaa.myplaces


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddPlace.setOnClickListener {
            val intent = Intent(this,AddPlaceActivity::class.java)
            startActivity(intent)
        }
        getPlacesList()
    }

    private fun getPlacesList(){
        val dbHandler = DataBaseHandler(this)
        val placesList = dbHandler.getMyPlacesList()
        
    }

}
