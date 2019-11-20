package pl.kopsoft.pczplan

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(appContext: Context) {
    private val prefs: SharedPreferences = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    var lastSelectedGroupName: String?
        get() {
            return prefs.getString(LAST_GROUP_NAME, null)
        }
        set(value) {
            editor.putString(LAST_GROUP_NAME, value)
            editor.apply()
        }

    companion object {
        const val PREFS = "pczplanprefs"
        const val LAST_GROUP_NAME = "lastsched"
    }
}