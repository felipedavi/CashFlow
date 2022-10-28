package meimaonamassa.cashflow.feature.transaction.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import meimaonamassa.cashflow.data.TransactionRepository

class TransactionViewModelFactory(private val repository: TransactionRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java))
            return TransactionViewModel(repository) as T
        throw IllegalArgumentException("ViewModel Not Found")
    }
}