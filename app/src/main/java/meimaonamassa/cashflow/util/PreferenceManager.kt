package meimaonamassa.cashflow.util

import android.content.Context
import androidx.core.content.edit

class PreferenceManager(context: Context) {
    private val sharedPref = context.getSharedPreferences("config_prefs", Context.MODE_PRIVATE)

    fun saveMonthlyBudget(budget: Float) {
        sharedPref.edit { putFloat("monthly_budget", budget) }
    }

    fun getMonthlyBudget(): Float {
        return sharedPref.getFloat("monthly_budget", 0f)
    }
}