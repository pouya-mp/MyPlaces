package com.pouyaa.myplaces

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.places_layout.view.*

open class PlacesAdapter(
    private var context: Context,
    private var arrayList: ArrayList<PlaceModel>
) : RecyclerView.Adapter<PlacesAdapter.myViewHolder>() {


    class myViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(LayoutInflater.from(context).inflate(R.layout.places_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val model = arrayList[position]

        if (holder is myViewHolder){
            holder.itemView.selectedImageOfPlaceImageView.setImageURI(Uri.parse(model.image))
            holder.itemView.placeTitleTextView.text = model.title
            holder.itemView.placeDescriptionTextView.text = model.description
        }
    }
}