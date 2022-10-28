package meimaonamassa.cashflow.feature.transaction.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import meimaonamassa.cashflow.data.TransactionRepository
import meimaonamassa.cashflow.data.entity.TransactionEntity
import kotlinx.coroutines.launch

class TransactionAddViewModel(private val repository: TransactionRepository) : ViewModel() {
    fun insert(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.insert(transaction)
        }
    }
}