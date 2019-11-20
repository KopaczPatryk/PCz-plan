package pl.kopsoft.pczplan.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.kopsoft.pczplan.PrefsManager

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var prefs: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PrefsManager(applicationContext)
    }
}