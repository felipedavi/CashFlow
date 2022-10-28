package meimaonamassa.cashflow.feature.transaction.detail

import androidx.lifecycle.*
import meimaonamassa.cashflow.data.TransactionRepository
import meimaonamassa.cashflow.data.entity.TransactionEntity
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