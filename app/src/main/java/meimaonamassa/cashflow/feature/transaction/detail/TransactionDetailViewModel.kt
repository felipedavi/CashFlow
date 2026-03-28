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

    fun updateGroup(transaction: TransactionEntity, newTotal: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO + NonCancellable) {
                val groupId = transaction.installmentGroupId ?: return@withContext
                val existingInstallments = repository.getTransactionsByGroupId(groupId).sortedBy { it.installmentCurrent }

                if (existingInstallments.isEmpty()) return@withContext

                val originalTotal = existingInstallments.size
                val editedCurrent = transaction.installmentCurrent ?: 1
                val editedDate = transaction.date

                for (existing in existingInstallments) {
                    if (existing.installmentCurrent!! <= newTotal) {
                        val monthOffset = (existing.installmentCurrent!! - editedCurrent).toLong()
                        val updatedDate = editedDate?.plusMonths(monthOffset)

                        val updated = existing.copy(
                            payerPayee = transaction.payerPayee,
                            description = transaction.description,
                            date = updatedDate,
                            monetaryValue = transaction.monetaryValue,
                            transactionType = transaction.transactionType,
                            installmentTotal = newTotal
                        )
                        repository.update(updated)
                    } else {
                        repository.delete(existing)
                    }
                }

                if (newTotal > originalTotal) {
                    for (i in (originalTotal + 1)..newTotal) {
                        val currentMonthOffset = (i - editedCurrent).toLong()
                        val newDate = editedDate?.plusMonths(currentMonthOffset)

                        val newInstallment = TransactionEntity(
                            id = 0,
                            payerPayee = transaction.payerPayee,
                            description = transaction.description,
                            date = newDate,
                            monetaryValue = transaction.monetaryValue,
                            transactionType = transaction.transactionType,
                            isInstallment = true,
                            installmentCurrent = i,
                            installmentTotal = newTotal,
                            installmentGroupId = groupId
                        )
                        repository.insert(newInstallment)
                    }
                }
            }
        }
    }
}