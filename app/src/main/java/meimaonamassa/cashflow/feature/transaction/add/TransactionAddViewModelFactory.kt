package meimaonamassa.cashflow.feature.transaction.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import meimaonamassa.cashflow.data.TransactionRepository

class TransactionAddViewModelFactory(private val repository: TransactionRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionAddViewModel::class.java))
            return TransactionAddViewModel(repository) as T
        throw IllegalArgumentException("ViewModel Not Found")
    }
}