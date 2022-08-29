package br.edu.ifrj.portal.cashflow.util.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.isDateValid(): Boolean {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).apply {
        this.isLenient = false
    }
    try {
        dateFormat.parse(this.trim())
    } catch (pe: ParseException) {
        return false
    }
    return true
}