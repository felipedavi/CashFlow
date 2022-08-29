package br.edu.ifrj.portal.cashflow.data.local

import androidx.room.*
import br.edu.ifrj.portal.cashflow.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {
    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)
}