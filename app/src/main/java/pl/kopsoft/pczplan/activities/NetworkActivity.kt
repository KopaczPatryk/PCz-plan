package pl.kopsoft.pczplan.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import pl.kopsoft.pczplan.R


abstract class NetworkActivity : BaseActivity() {
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onNetworkAvailable()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            onNetworkUnavailable()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = findViewById<ViewGroup>(android.R.id.content)
        snackbar = Snackbar.make(root, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
    }

    override fun onResume() {
        super.onResume()
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        if (isConnected()) {
            onNetworkAvailable()
        } else {
            onNetworkUnavailable()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    protected fun isConnected(): Boolean {
        return connectivityManager.activeNetwork != null
    }

    open fun onNetworkAvailable() {
        snackbar.dismiss()
    }

    open fun onNetworkUnavailable() {
        snackbar.show()
    }
}