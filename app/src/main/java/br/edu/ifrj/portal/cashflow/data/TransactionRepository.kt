package br.edu.ifrj.portal.cashflow.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import br.edu.ifrj.portal.cashflow.data.entity.TransactionEntity
import br.edu.ifrj.portal.cashflow.data.local.TransactionDAO
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDAO) {
    @WorkerThread
    suspend fun insert(transaction: TransactionEntity) {
        dao.insert(transaction)
    }

    fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return dao.getAllTransactions()
    }

    fun get(id: Int): LiveData<TransactionEntity> = dao.getById(id)

    @WorkerThread
    suspend fun update(transaction: TransactionEntity) {
        dao.update(transaction)
    }

    @WorkerThread
    suspend fun delete(transaction: TransactionEntity) {
        dao.delete(transaction)
    }
}