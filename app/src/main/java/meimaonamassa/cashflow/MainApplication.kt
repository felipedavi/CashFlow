package meimaonamassa.cashflow

import android.app.Application
import meimaonamassa.cashflow.base.TransactionDatabase
import meimaonamassa.cashflow.data.TransactionRepository
import com.jakewharton.threetenabp.AndroidThreeTen

class MainApplication: Application() {

    private val database by lazy { TransactionDatabase.getDatabase(this@MainApplication) }
    val repository by lazy { TransactionRepository(database.transactionDAO()) }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        instance = this
    }

    companion object {
        lateinit var instance: MainApplication
            private set
    }

}