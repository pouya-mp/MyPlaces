package com.pouyaa.myplaces

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import java.text.SimpleDateFormat
import java.util.*

class PickDate(editText: AppCompatEditText) {
    private var dateEditText = editText
    private var cal = Calendar.getInstance()

    private var dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        setCalendarValues(year, month, dayOfMonth)
        updateDateEditText()
    }

    fun selectDate(context: Context){
        DatePickerDialog(
            context, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setCalendarValues(inputYear: Int, inputMonth: Int, inputDayOfMonth: Int) {

        cal.set(Calendar.YEAR, inputYear)
        cal.set(Calendar.MONTH, inputMonth)
        cal.set(Calendar.DAY_OF_MONTH, inputDayOfMonth)


    }

    private fun updateDateEditText() {

        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        dateEditText.setText(sdf.format(cal.time)).toString()

    }

}