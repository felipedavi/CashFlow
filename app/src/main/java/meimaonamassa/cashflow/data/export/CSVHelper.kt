package meimaonamassa.cashflow.data.export

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
    private const val HEADER =
        "payerPayee${DELIMITER}description${DELIMITER}date${DELIMITER}monetaryValue${DELIMITER}transactionType${DELIMITER}isInstallment${DELIMITER}installmentCurrent${DELIMITER}installmentTotal"

    fun exportTransactions(transactions: List<TransactionEntity>): String {
        val rows = transactions.map { transaction ->
            listOf(
                escapeCSV(transaction.payerPayee),
                escapeCSV(transaction.description),
                transaction.date?.format(formatter) ?: "",
                transaction.monetaryValue.toString(),
                transaction.transactionType.toString(),
                transaction.isInstallment.toString(),
                transaction.installmentCurrent?.toString() ?: "",
                transaction.installmentTotal?.toString() ?: ""
            ).joinToString(DELIMITER)
        }

        return buildString {
            append(HEADER)
            append("\n")
            if (rows.isNotEmpty()) {
                append(rows.joinToString("\n"))
                append("\n")
            }
        }
    }

    fun importTransactions(inputStream: InputStream): List<TransactionEntity> {
        val transactions = mutableListOf<TransactionEntity>()
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.use { reader ->
            reader.readLine()

            var line: String? = reader.readLine()
            while (line != null) {
                val tokens = parseCSVLine(line)
                if (tokens.size >= 5) {
                    try {
                        val dateStr = tokens[2]
                        val parsedDate = if (dateStr.isNotEmpty()) {
                            LocalDate.parse(dateStr, formatter).atStartOfDay(ZoneOffset.UTC)
                                .toOffsetDateTime()
                        } else {
                            null
                        }

                        val isInstallment = if (tokens.size > 5) tokens[5].toBoolean() else false
                        val installmentCurrent =
                            if (tokens.size > 6 && tokens[6].isNotEmpty()) tokens[6].toIntOrNull() else null
                        val installmentTotal =
                            if (tokens.size > 7 && tokens[7].isNotEmpty()) tokens[7].toIntOrNull() else null

                        val entity = TransactionEntity(
                            id = 0,
                            payerPayee = tokens[0],
                            description = tokens[1],
                            date = parsedDate,
                            monetaryValue = tokens[3].toDoubleOrNull() ?: 0.0,
                            transactionType = tokens[4].toBoolean(),
                            isInstallment = isInstallment,
                            installmentCurrent = installmentCurrent,
                            installmentTotal = installmentTotal
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

    private fun escapeCSV(value: String): String {
        if (value.contains(DELIMITER) || value.contains("\"") || value.contains("\n")) {
            return "\"${value.replace("\"", "\"\"")}\""
        }
        return value
    }

    private fun parseCSVLine(line: String): List<String> {
        val tokens = mutableListOf<String>()
        var currentToken = StringBuilder()
        var inQuotes = false

        for (i in line.indices) {
            val char = line[i]
            if (char == '"') {
                if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                    continue
                } else if (i > 0 && line[i - 1] == '"' && inQuotes) {
                    currentToken.append('"')
                } else {
                    inQuotes = !inQuotes
                }
            } else if (char.toString() == DELIMITER && !inQuotes) {
                tokens.add(currentToken.toString())
                currentToken = StringBuilder()
            } else {
                currentToken.append(char)
            }
        }
        tokens.add(currentToken.toString())
        return tokens
    }
}