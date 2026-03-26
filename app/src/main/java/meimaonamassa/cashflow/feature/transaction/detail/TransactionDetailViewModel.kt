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

    fun updateGroup(transaction: TransactionEntity, newTotal: Int) {
        viewModelScope.launch {
            val groupId = transaction.installmentGroupId ?: return@launch
            val existingInstallments = repository.getTransactionsByGroupId(groupId).sortedBy { it.installmentCurrent }

            if (existingInstallments.isEmpty()) return@launch

            val originalTotal = existingInstallments.size
            val editedCurrent = transaction.installmentCurrent ?: 1
            val editedDate = transaction.date // A nova data que foi salva na tela
            val newValuePerInstallment = transaction.monetaryValue

            for (existing in existingInstallments) {
                if (existing.installmentCurrent!! <= newTotal) {
                    val updatedDesc = transaction.description.substringBefore(" - Par.") + " - Par. ${existing.installmentCurrent}/$newTotal"

                    val monthOffset = (existing.installmentCurrent!! - editedCurrent).toLong()
                    val updatedDate = editedDate?.plusMonths(monthOffset)

                    val updated = existing.copy(
                        payerPayee = transaction.payerPayee,
                        description = updatedDesc,
                        date = updatedDate,
                        monetaryValue = newValuePerInstallment,
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
                    val newDesc = transaction.description.substringBefore(" - Par.") + " - Par. $i/$newTotal"

                    val newInstallment = TransactionEntity(
                        id = 0,
                        payerPayee = transaction.payerPayee,
                        description = newDesc,
                        date = newDate,
                        monetaryValue = newValuePerInstallment,
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