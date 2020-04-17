package com.pouyaa.myplaces


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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

        if (placesList.size > 0){
            placesListRecycleView.visibility = View.VISIBLE
            noPlacesAddedYetTextView.visibility = View.GONE
            setupMyPlacesRecycleView(placesList)
        }else {
            placesListRecycleView.visibility = View.GONE
            noPlacesAddedYetTextView.visibility = View.VISIBLE

        }

    }

    private fun setupMyPlacesRecycleView(myPlacesList: ArrayList<PlaceModel>){
        placesListRecycleView.layoutManager = LinearLayoutManager(this)
        placesListRecycleView.setHasFixedSize(true)

        val placesAdapter = PlacesAdapter(this,myPlacesList)
        placesListRecycleView.adapter = placesAdapter
    }

}
