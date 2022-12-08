package com.test.cdcn_appmobile.ui.dialog
/*
 * Created by tuyen.dang on 11/9/2022
 */
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.test.cdcn_appmobile.extension.toDayMonth
import java.util.*

class DatePickerFragment(private val onDateChoice: (String) -> Unit) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the USER
        onDateChoice(day.toString().toDayMonth() + "-" + (month + 1).toString().toDayMonth() + "-" + year)
        dismiss()
    }
}