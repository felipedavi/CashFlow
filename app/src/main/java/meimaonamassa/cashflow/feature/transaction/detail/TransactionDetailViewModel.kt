package meimaonamassa.cashflow.feature.transaction.detail

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import meimaonamassa.cashflow.data.TransactionRepository
import meimaonamassa.cashflow.data.entity.TransactionEntity

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
            withContext(Dispatchers.IO + NonCancellable) {
                repository.update(transaction)
            }
        }
    }
}