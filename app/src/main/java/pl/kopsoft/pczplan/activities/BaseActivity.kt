package pl.kopsoft.pczplan.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import pl.kopsoft.pczplan.PrefsManager

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var prefs: PrefsManager
    protected lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PrefsManager(applicationContext)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }
}