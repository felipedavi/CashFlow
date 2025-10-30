package meimaonamassa.cashflow.util.extension

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private val onlyNumberRegex by lazy { "[^0-9 ]".toRegex() }
private const val DECIMAL_FACTOR = 100
private const val CURRENCY_PATTERN = "R$ #,###,##0.00"
private val CURRENCY_PATTERN_BRAZIL: DecimalFormatSymbols = DecimalFormatSymbols(
    Locale.forLanguageTag("pt-BR")
).also {
    it.decimalSeparator = ','
    it.groupingSeparator = '.'
}

fun String.fromCurrency(): Double = this
    .replace(onlyNumberRegex, "")
    .toDouble()
    .div(DECIMAL_FACTOR)

fun Double.toCurrency(): String = DecimalFormat(CURRENCY_PATTERN, CURRENCY_PATTERN_BRAZIL)
    .format(this)