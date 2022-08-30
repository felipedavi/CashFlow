package br.edu.ifrj.portal.cashflow.feature.transaction.detail

import androidx.lifecycle.*
import br.edu.ifrj.portal.cashflow.data.TransactionRepository
import br.edu.ifrj.portal.cashflow.data.entity.TransactionEntity
import kotlinx.coroutines.launch

class TransactionDetailViewModel(private val repository: TransactionRepository) : ViewModel() {
    private val _id = MutableLiveData<Int>()
    private var _transaction: LiveData<TransactionEntity> = _id
        .switchMap { id ->
            repository.get(id)
        }

    val transaction = _transaction

    fun start(id: Int) {
        _id.value = id
    }

    fun update(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.update(transaction)
        }
    }

}