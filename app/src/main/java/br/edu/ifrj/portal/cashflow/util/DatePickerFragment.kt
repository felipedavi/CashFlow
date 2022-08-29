package br.edu.ifrj.portal.cashflow.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import br.edu.ifrj.portal.cashflow.util.extension.isDateValid
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment(private val inDate: String, val callback: (result: String) -> Unit) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year: Int
        val month: Int
        val day: Int
        if (inDate.isDateValid()) {
            val d = inDate.split("/")
            year = Integer.parseInt(d[2])
            month = Integer.parseInt(d[1])-1
            day = Integer.parseInt(d[0])
        } else {
            val c: Calendar = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day= c.get(Calendar.DAY_OF_MONTH)
        }
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val newDate = Calendar.getInstance()
        newDate.set(year, month, day)
        val selectedDate = formatter.format(newDate.time)
        callback(selectedDate)
    }
}