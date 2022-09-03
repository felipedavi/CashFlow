package br.edu.ifrj.portal.cashflow

import android.app.Application
import br.edu.ifrj.portal.cashflow.base.TransactionDatabase
import br.edu.ifrj.portal.cashflow.data.TransactionRepository
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