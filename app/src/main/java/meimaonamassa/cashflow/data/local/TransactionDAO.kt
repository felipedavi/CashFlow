package meimaonamassa.cashflow.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import meimaonamassa.cashflow.data.entity.TransactionEntity

@Dao
interface TransactionDAO {
    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Query("SELECT * FROM transaction_table ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transaction_table WHERE transaction_id = :id")
    fun getById(id: Int): LiveData<TransactionEntity>

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT SUM(monetary_value) FROM transaction_table WHERE transaction_type = 1")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(monetary_value) FROM transaction_table WHERE transaction_type = 0")
    fun getTotalExpense(): Flow<Double?>
}