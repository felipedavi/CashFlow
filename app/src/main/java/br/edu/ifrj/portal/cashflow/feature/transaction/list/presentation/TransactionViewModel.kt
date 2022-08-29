package br.edu.ifrj.portal.cashflow.feature.transaction.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifrj.portal.cashflow.data.TransactionRepository
import br.edu.ifrj.portal.cashflow.data.entity.TransactionEntity
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository): ViewModel() {
    val allTransactions: LiveData<List<TransactionEntity>> = repository.getAllTransactions().asLiveData()

    fun delete(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }
}