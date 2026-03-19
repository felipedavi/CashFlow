package meimaonamassa.cashflow.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import meimaonamassa.cashflow.data.TransactionRepository
import meimaonamassa.cashflow.data.export.CSVHelper
import java.io.InputStream
import java.io.OutputStream

class SettingsViewModel(private val repository: TransactionRepository) : ViewModel() {

    fun exportData(outputStream: OutputStream, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val transactions = repository.getAllTransactionsStatic()
                val csvString = CSVHelper.exportTransactions(transactions)
                outputStream.use { it.write(csvString.toByteArray(Charsets.UTF_8)) }
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun importData(inputStream: InputStream, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val transactions = CSVHelper.importTransactions(inputStream)
                transactions.forEach { repository.insert(it) }
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}