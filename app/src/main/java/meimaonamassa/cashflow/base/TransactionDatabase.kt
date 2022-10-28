package meimaonamassa.cashflow.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import meimaonamassa.cashflow.data.entity.TransactionEntity
import meimaonamassa.cashflow.data.local.TransactionDAO

@Database(entities = [TransactionEntity::class], version = 2)
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