package com.example.yandextodo.core.utils

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class DatePickerHelper(
    private val context: Context,
    private val onDateSelected: (String) -> Unit
) {
    fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
                val formattedDate = dateFormat.format(selectedCalendar.time)
                onDateSelected(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
