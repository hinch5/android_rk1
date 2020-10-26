package com.bmstu.shatnyuk.androidrk1

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bmstu.shatnyuk.androidrk1.model.MarketData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bmstu.shatnyuk.androidrk1.databinding.FragmentHostBinding
import java.lang.Exception

class HostFragment : Fragment() {
    private var _binding: FragmentHostBinding? = null

    private val binding
        get(): FragmentHostBinding {
            return _binding ?: throw Exception("null binding")
        }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHostBinding.inflate(inflater, container, false)
        val marketDataList = loadMarketData("BTC", "USDT", 100)
        viewManager = LinearLayoutManager(context)
        viewAdapter = MarketDataAdapter(marketDataList)
        recyclerView = binding.marketDataRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        setLink()
        binding.link.setOnClickListener { this.openBinance() }
        return binding.root

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

    private fun setLink() {
        val content = SpannableString("https://www.binance.com/ru/trade/BTC_USDT")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        binding.link.text = content
    }

    private fun openBinance() {
        val webpage: Uri = Uri.parse(binding.link.text.toString())
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(activity?.packageManager!!) != null) {
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HostFragment()
    }
}