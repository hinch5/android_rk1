package com.bmstu.shatnyuk.androidrk1

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.bmstu.shatnyuk.androidrk1.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val marketDataListViewModel: MarketDataListViewModel by viewModels()
    private val baseQuoteViewModel: BaseQuoteViewModel by viewModels()
    private val baseAssetKey = "baseAsset"
    private val defaultBaseAsset = "BTC"
    private lateinit var baseAsset: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.root)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        val baseAssetSaved = savedInstanceState?.getString(baseAssetKey) ?: defaultBaseAsset
        baseQuoteViewModel.baseAssetInput(baseAssetSaved)
        baseQuoteViewModel.getBaseAsset().observe(this, Observer { asset ->
            baseAsset = asset
            marketDataListViewModel.refreshMarketData(
                baseAsset,
                getQuote(),
                getDaysQty().toInt()
            )
        })
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putString(baseAssetKey, baseAsset)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.action_refresh) {
            marketDataListViewModel.refreshMarketData(baseAsset, getQuote(), getDaysQty().toInt())
        } else if (itemId == R.id.action_settings) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            if (navController.currentDestination != null) {
                if (navController.currentDestination!!.id == R.id.hostFragment) {
                    navController.navigate(R.id.action_hostFragment_to_settingsActivity)
                } else if (navController.currentDestination!!.id == R.id.detailsFragment) {
                    navController.navigate(R.id.action_detailFragment_to_settingsActivity)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    fun getQuote(): String {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getString("fiat_currency", "USDT")!!
    }

    fun getDaysQty(): Long {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val qty: String = sharedPreferences.getString("days_qty", "1")!!
        return qty.toLong()
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<EditTextPreference>("days_qty")!!.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference, newValue ->
                    onPreferenceChange(preference, newValue)
                }
        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            if (preference!!.key.equals("days_qty")) {
                try {
                    val value: Long = (newValue as String).toLong()
                    if (value <= 0) {
                        Toast.makeText(
                            context,
                            "required positive number",
                            Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                    return true;
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "invalid number format",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
            return true;
        }
    }
}