package br.edu.ifrj.portal.cashflow.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.edu.ifrj.portal.cashflow.data.entity.TransactionEntity
import br.edu.ifrj.portal.cashflow.data.local.TransactionDAO

@Database(entities = [TransactionEntity::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDAO() : TransactionDAO

    companion object {
        private var instance: TransactionDatabase? = null

        fun getDatabase(context: Context): TransactionDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            TransactionDatabase::class.java, "transaction_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}