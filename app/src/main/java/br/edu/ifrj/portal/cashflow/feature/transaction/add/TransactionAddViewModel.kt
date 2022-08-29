package br.edu.ifrj.portal.cashflow.feature.transaction.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifrj.portal.cashflow.data.TransactionRepository
import br.edu.ifrj.portal.cashflow.data.entity.TransactionEntity
import kotlinx.coroutines.launch

class TransactionAddViewModel(private val repository: TransactionRepository) : ViewModel() {
    fun insert(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.insert(transaction)
        }
    }
}