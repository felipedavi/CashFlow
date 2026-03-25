package meimaonamassa.cashflow.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import meimaonamassa.cashflow.data.entity.TransactionEntity

@Dao
interface TransactionDAO {
    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Query("SELECT * FROM transaction_table WHERE transaction_id = :id")
    fun getTransactionById(id: Int): LiveData<TransactionEntity>

    @Query("SELECT * FROM transaction_table ORDER BY date DESC")
    suspend fun getAllTransactionsStatic(): List<TransactionEntity>

    @Query("SELECT * FROM transaction_table WHERE date LIKE :monthPrefix || '%' ORDER BY date DESC")
    fun getTransactionsByMonth(monthPrefix: String): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(monetary_value) FROM transaction_table WHERE transaction_type = 1 AND date LIKE :monthPrefix || '%'")
    fun getTotalIncomeByMonth(monthPrefix: String): Flow<Double?>

    @Query("SELECT SUM(monetary_value) FROM transaction_table WHERE transaction_type = 0 AND date LIKE :monthPrefix || '%'")
    fun getTotalExpenseByMonth(monthPrefix: String): Flow<Double?>

    @Query("SELECT * FROM transaction_table WHERE installment_group_id = :groupId")
    suspend fun getTransactionsByGroupId(groupId: String): List<TransactionEntity>

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transaction_table WHERE installment_group_id = :groupId")
    suspend fun deleteTransactionGroup(groupId: String)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAllTransactions()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'transaction_table'")
    suspend fun resetTransactionPrimaryKeySequence()
}