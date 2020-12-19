package com.bmstu.shatnyuk.androidrk1

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bmstu.shatnyuk.androidrk1.databinding.FragmentHostBinding
import com.bmstu.shatnyuk.androidrk1.model.MarketData

class HostFragment : Fragment(), AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentHostBinding? = null

    private val binding
        get(): FragmentHostBinding {
            return _binding ?: throw Exception("null binding")
        }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var baseAsset: String? = null
    private val baseQuoteViewModel: BaseQuoteViewModel by activityViewModels()
    private val marketDataListViewModel: MarketDataListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHostBinding.inflate(inflater, container, false)
        binding.link.setOnClickListener { this.openBinance() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(requireContext())
        val viewAdapter = MarketDataAdapter(
            this
        ) { hostFragment: HostFragment, marketData: MarketData ->
            val navController = hostFragment.findNavController()
            navController.navigate(
                R.id.action_hostFragment_to_detailsFragment,
                bundleOf("data" to marketData)
            )
        }
        val recyclerView = binding.marketDataRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        marketDataListViewModel.getMarketDataList()
            .observe(viewLifecycleOwner, Observer { marketDataList ->
                viewAdapter.data = marketDataList.toTypedArray()
            })

        marketDataListViewModel.getRefreshing()
            .observe(viewLifecycleOwner, Observer { refreshing ->
                Log.i("REFRESHING", refreshing.toString())
                if (refreshing) {
                    recyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.marketDataSwipe.isRefreshing = true
                } else {
                    recyclerView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.marketDataSwipe.isRefreshing = false
                }
            })
        var baseAsset = baseQuoteViewModel.getBaseAsset().value
        if (baseAsset == null) {
            baseAsset = "BTC"
            baseQuoteViewModel.baseAssetInput(baseAsset)
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.base_currency_values,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.baseAssetInput.adapter = it
            val v = baseAsset
            binding.baseAssetInput.setSelection(it.getPosition(v))
            binding.baseAssetInput.onItemSelectedListener = this
        }
        binding.marketDataSwipe.setOnRefreshListener(this)
    }

    private fun setLink(baseAsset: String, quoteAsset: String) {
        val content = SpannableString("https://www.binance.com/ru/trade/${baseAsset}_${quoteAsset}")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        binding.link.text = content
    }

    private fun getQuote(): String {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        return sharedPreferences.getString("fiat_currency", "")!!
    }

    private fun getDaysQty(): Long {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        val qty: String = sharedPreferences.getString("days_qty", "1")!!
        return qty.toLong()
    }

    private fun openBinance() {
        val webpage: Uri = Uri.parse(binding.link.text.toString())
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(activity?.packageManager!!) != null) {
            startActivity(intent)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val value = parent.getItemAtPosition(pos)
        baseAsset = value.toString()
        setLink(value.toString(), getQuote())
        baseQuoteViewModel.baseAssetInput(value.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

    override fun onRefresh() {
        marketDataListViewModel.refreshMarketData(
            baseAsset!!,
            getQuote(),
            getDaysQty().toInt()
        )
    }
}