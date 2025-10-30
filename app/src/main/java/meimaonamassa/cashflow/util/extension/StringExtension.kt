package meimaonamassa.cashflow.util.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.isDateValid(): Boolean {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")).apply {
        this.isLenient = false
    }
    try {
        dateFormat.parse(this.trim())
    } catch (_: ParseException) {
        return false
    }
    return true
}

fun String.toFormattedDate(): String {
    val date = this.split('/')
    return "${date[2]}-${date[1]}-${date[0]}"
}

fun String.fromFormattedDate(): String {
    val date = this.split('-')
    return "${date[2]}/${date[1]}/${date[0]}"
}