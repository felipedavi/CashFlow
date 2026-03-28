package meimaonamassa.cashflow.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import meimaonamassa.cashflow.data.entity.TransactionEntity
import meimaonamassa.cashflow.data.export.CSVHelper
import meimaonamassa.cashflow.data.local.TransactionDAO
import java.io.InputStream

class TransactionRepository(private val dao: TransactionDAO) {
    @WorkerThread
    suspend fun insert(transaction: TransactionEntity) {
        dao.insert(transaction)
    }

    suspend fun getAllTransactionsStatic(): List<TransactionEntity> {
        return dao.getAllTransactionsStatic()
    }

    fun getTransactionsByMonth(monthPrefix: String) = dao.getTransactionsByMonth(monthPrefix)

    fun getTotalIncomeByMonth(monthPrefix: String) = dao.getTotalIncomeByMonth(monthPrefix)

    fun getTotalExpenseByMonth(monthPrefix: String) = dao.getTotalExpenseByMonth(monthPrefix)

    fun exportTransactionsToCSV(transactions: List<TransactionEntity>): String {
        return CSVHelper.exportTransactions(transactions)
    }

    fun importTransactionsFromCSV(inputStream: InputStream): List<TransactionEntity> {
        return CSVHelper.importTransactions(inputStream)
    }

    suspend fun deleteAll() {
        dao.deleteAllTransactions()
        dao.resetTransactionPrimaryKeySequence()
    }

    fun get(id: Int): LiveData<TransactionEntity> = dao.getTransactionById(id)

    @WorkerThread
    suspend fun update(transaction: TransactionEntity) {
        dao.updateTransaction(transaction)
    }

    @WorkerThread
    suspend fun delete(transaction: TransactionEntity) {
        dao.deleteTransaction(transaction)
    }
}