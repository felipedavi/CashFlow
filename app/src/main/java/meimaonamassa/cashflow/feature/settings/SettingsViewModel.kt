package meimaonamassa.cashflow.feature.settings // Ajuste o pacote conforme sua estrutura

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import meimaonamassa.cashflow.data.TransactionRepository
import meimaonamassa.cashflow.util.CSVHelper
import java.io.InputStream
import java.io.OutputStream

class SettingsViewModel(private val repository: TransactionRepository) : ViewModel() {

    fun exportData(outputStream: OutputStream, function: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val transactions = repository.getAllTransactionsList()
                val csvData = CSVHelper.exportTransactions(transactions)
                outputStream.use { it.write(csvData.toByteArray()) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun importData(inputStream: InputStream, function: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val transactions = CSVHelper.importTransactions(inputStream)
                transactions.forEach { repository.insert(it) }
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