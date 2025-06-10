package meimaonamassa.cashflow.feature.transaction.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import meimaonamassa.cashflow.data.TransactionRepository
import meimaonamassa.cashflow.data.entity.TransactionEntity
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository): ViewModel() {
    val allTransactions: LiveData<List<TransactionEntity>> = repository.getAllTransactions().asLiveData()

    val totalIncome: LiveData<Double> = repository.getTotalIncome()
        .map { it ?: 0.0 }
        .asLiveData()

    val totalExpense: LiveData<Double> = repository.getTotalExpense()
        .map { it ?: 0.0 }
        .asLiveData()

    val balance: LiveData<Double> = MediatorLiveData<Double>().apply {
        value = (totalIncome.value ?: 0.0) - (totalExpense.value ?: 0.0)

        addSource(totalIncome) { income ->
            value = income - (totalExpense.value ?: 0.0)
        }
        addSource(totalExpense) { expense ->
            value = (totalIncome.value ?: 0.0) - expense
        }
    }

    fun delete(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }
}