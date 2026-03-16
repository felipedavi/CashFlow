package meimaonamassa.cashflow.util

import meimaonamassa.cashflow.data.entity.TransactionEntity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

object CSVHelper {
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private const val DELIMITER = ";"
    private const val HEADER = "id${DELIMITER}payerPayee${DELIMITER}description${DELIMITER}date${DELIMITER}monetaryValue${DELIMITER}transactionType"

    fun exportTransactions(transactions: List<TransactionEntity>): String {
        val csv = StringBuilder()
        csv.append("$HEADER\n")

        transactions.forEach {
            csv.append("${it.id}$DELIMITER")
            csv.append("${it.payerPayee}$DELIMITER")
            csv.append("${it.description}$DELIMITER")
            val dateStr = it.date?.format(formatter) ?: ""
            csv.append("$dateStr$DELIMITER")
            csv.append("${it.monetaryValue}$DELIMITER")
            csv.append("${it.transactionType}\n")
        }
        return csv.toString()
    }

    fun importTransactions(inputStream: InputStream): List<TransactionEntity> {
        val transactions = mutableListOf<TransactionEntity>()
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.use { reader ->
            reader.readLine()

            var line: String? = reader.readLine()
            while (line != null) {
                val tokens = line.split(DELIMITER)
                if (tokens.size >= 6) {
                    try {
                        val dateStr = tokens[3]
                        val parsedDate = if (dateStr.isNotEmpty()) {
                            LocalDate.parse(dateStr, formatter).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime()
                        } else {
                            null
                        }

                        val entity = TransactionEntity(
                            id = 0,
                            payerPayee = tokens[1],
                            description = tokens[2],
                            date = parsedDate,
                            monetaryValue = tokens[4].toDoubleOrNull() ?: 0.0,
                            transactionType = tokens[5].toBoolean()
                        )
                        transactions.add(entity)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                line = reader.readLine()
            }
        }
        return transactions
    }
}