package meimaonamassa.cashflow.feature.transaction.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import meimaonamassa.cashflow.data.TransactionRepository
import meimaonamassa.cashflow.data.entity.TransactionEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionAddViewModel(private val repository: TransactionRepository) : ViewModel() {
    fun insert(transaction: TransactionEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO + NonCancellable) {
                repository.insert(transaction)
            }
        }
    }
}