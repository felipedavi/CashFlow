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
                val transactionsList = repository.getAllTransactionsStatic()
                val csvData = CSVHelper.exportTransactions(transactionsList)

                outputStream.bufferedWriter().use { writer ->
                    writer.write(csvData)
                }

                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun importData(inputStream: InputStream, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val transactions = CSVHelper.importTransactions(inputStream)

                if (transactions.isNotEmpty()) {
                    transactions.forEach { repository.insert(it) }
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onError()
                }
            }
        }
    }

    fun clearAllData(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }
}