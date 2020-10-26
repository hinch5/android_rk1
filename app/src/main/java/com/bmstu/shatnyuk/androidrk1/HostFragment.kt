package com.bmstu.shatnyuk.androidrk1

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bmstu.shatnyuk.androidrk1.model.MarketData

class HostFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host, container, false)
    }

    fun navigateToDetail(data: MarketData) {
        val navController = this.findNavController()
        navController.navigate(
            R.id.action_hostFragment_to_detailsFragment,
            bundleOf("data" to data)
        )
    }

    fun sendRefreshedData(data: MarketData) {
        setFragmentResult(ARG1, bundleOf("data" to data))
    }

    fun getQuote(): String {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString("fiat_currency", "")!!
    }

    fun getDaysQty(): Long {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val qty: String = sharedPreferences.getString("days_qty", "")!!
        return qty.toLong()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HostFragment()
    }
}