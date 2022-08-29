package br.edu.ifrj.portal.cashflow

import android.app.Application

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MainApplication
            private set
    }

}