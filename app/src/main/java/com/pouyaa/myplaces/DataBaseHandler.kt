package com.pouyaa.myplaces

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createMyPlaceTable = ("CREATE TABLE " + TABLE_MY_PLACES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(createMyPlaceTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MY_PLACES")
        onCreate(db)
    }

    fun addMyPlace(placeModel: PlaceModel): Long {

        val dbHandler = writableDatabase

        val contentValues = ContentValues()

        contentValues.put(KEY_TITLE, placeModel.title)
        contentValues.put(KEY_IMAGE, placeModel.image)
        contentValues.put(
            KEY_DESCRIPTION,
            placeModel.description
        )
        contentValues.put(KEY_DATE, placeModel.date)
        contentValues.put(KEY_LOCATION, placeModel.location)
        contentValues.put(KEY_LATITUDE, placeModel.latitude)
        contentValues.put(KEY_LONGITUDE, placeModel.longitude)

        val result = dbHandler.insert(TABLE_MY_PLACES, null, contentValues)
        dbHandler.close()
        return result

    }

    fun getMyPlacesList(): ArrayList<PlaceModel> {
        val myPlacesList = ArrayList<PlaceModel>()
        val dbHandler = readableDatabase

        try {
            val cursor = dbHandler.rawQuery("SELECT * FROM $TABLE_MY_PLACES", null)
            if (cursor.moveToFirst()) {
                do {

                    val place = PlaceModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                        , cursor.getString(cursor.getColumnIndex(KEY_IMAGE))
                        , cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))
                        , cursor.getString(cursor.getColumnIndex(KEY_DATE))
                        , cursor.getString(cursor.getColumnIndex(KEY_LOCATION))
                        , cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE))
                        , cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))

                    )
                    myPlacesList.add(place)

                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            e.printStackTrace()
            return ArrayList()
        }

        return myPlacesList
    }

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "MyPlacesDatabase" // Database name
        private const val TABLE_MY_PLACES = "MyPlacesTable" // Table Name

        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }
}