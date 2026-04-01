package meimaonamassa.cashflow.util

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit
import androidx.core.content.edit

object ReviewHelper {
    private const val PREF_NAME = "review_prefs"
    private const val KEY_TOTAL_OPENS = "total_opens"
    private const val KEY_WEEKLY_OPENS = "weekly_opens"
    private const val KEY_LAST_WEEK_RESET = "last_week_reset"
    private const val KEY_ALREADY_REVIEWED = "already_reviewed"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun updateLaunchCount(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit {

            val totalOpens = prefs.getInt(KEY_TOTAL_OPENS, 0) + 1
            putInt(KEY_TOTAL_OPENS, totalOpens)

            val currentTime = System.currentTimeMillis()
            val lastReset = prefs.getLong(KEY_LAST_WEEK_RESET, 0L)

            if (currentTime - lastReset > TimeUnit.DAYS.toMillis(7)) {
                putInt(KEY_WEEKLY_OPENS, 1)
                putLong(KEY_LAST_WEEK_RESET, currentTime)
            } else {
                val weeklyOpens = prefs.getInt(KEY_WEEKLY_OPENS, 0) + 1
                putInt(KEY_WEEKLY_OPENS, weeklyOpens)
            }

        }
    }

    fun shouldShowReview(context: Context): Boolean {
        val prefs = getPrefs(context)

        if (prefs.getBoolean(KEY_ALREADY_REVIEWED, false)) return false

        val totalOpens = prefs.getInt(KEY_TOTAL_OPENS, 0)
        val weeklyOpens = prefs.getInt(KEY_WEEKLY_OPENS, 0)

        return totalOpens == 3 || weeklyOpens >= 10
    }

    fun markAsReviewed(context: Context) {
        getPrefs(context).edit { putBoolean(KEY_ALREADY_REVIEWED, true) }
    }
}