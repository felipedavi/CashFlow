package br.edu.ifrj.portal.cashflow.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import br.edu.ifrj.portal.cashflow.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

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
}