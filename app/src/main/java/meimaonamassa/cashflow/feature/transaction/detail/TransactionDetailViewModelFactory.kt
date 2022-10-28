package meimaonamassa.cashflow.feature.transaction.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import meimaonamassa.cashflow.data.TransactionRepository

class TransactionDetailViewModelFactory(private val repository: TransactionRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionDetailViewModel::class.java))
            return TransactionDetailViewModel(repository) as T
        throw IllegalArgumentException("ViewModel Not Found")
    }
}