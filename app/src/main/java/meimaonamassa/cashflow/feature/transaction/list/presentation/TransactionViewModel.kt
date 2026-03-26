package meimaonamassa.cashflow.feature.transaction.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import meimaonamassa.cashflow.data.TransactionRepository
import meimaonamassa.cashflow.data.entity.TransactionEntity
import org.threeten.bp.YearMonth

class TransactionViewModel(private val repository: TransactionRepository): ViewModel() {

    val currentMonth = MutableStateFlow(YearMonth.now())

    private val monthPrefixFlow = currentMonth.map {
        "${it.year}-${it.monthValue.toString().padStart(2, '0')}"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val allTransactions: LiveData<List<TransactionEntity>> = monthPrefixFlow
        .flatMapLatest { prefix -> repository.getTransactionsByMonth(prefix) }
        .asLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalIncome: LiveData<Double> = monthPrefixFlow
        .flatMapLatest { prefix -> repository.getTotalIncomeByMonth(prefix) }
        .map { it ?: 0.0 }
        .asLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalExpense: LiveData<Double> = monthPrefixFlow
        .flatMapLatest { prefix -> repository.getTotalExpenseByMonth(prefix) }
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

    fun nextMonth() {
        currentMonth.value = currentMonth.value.plusMonths(1)
    }

    fun previousMonth() {
        currentMonth.value = currentMonth.value.minusMonths(1)
    }

    fun delete(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }
    fun deleteGroup(groupId: String) {
        viewModelScope.launch {
            repository.deleteTransactionGroup(groupId)
        }
    }
}