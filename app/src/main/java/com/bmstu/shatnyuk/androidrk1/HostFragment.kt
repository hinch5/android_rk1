package com.bmstu.shatnyuk.androidrk1

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bmstu.shatnyuk.androidrk1.databinding.FragmentHostBinding
import com.bmstu.shatnyuk.androidrk1.model.MarketData

class HostFragment : Fragment() {
    private var _binding: FragmentHostBinding? = null

    private val binding
        get(): FragmentHostBinding {
            return _binding ?: throw Exception("null binding")
        }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var baseAsset: String

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
        val marketDataListViewModel: MarketDataListViewModel by activityViewModels()
        marketDataListViewModel.getMarketDataList()
            .observe(viewLifecycleOwner, Observer { marketDataList ->
                setLink(baseAsset, getQuote())
                viewManager = LinearLayoutManager(context)
                viewAdapter = MarketDataAdapter(
                    marketDataList,
                    this
                ) { hostFragment: HostFragment, marketData: MarketData ->
                    val navController = hostFragment.findNavController()
                    navController.navigate(
                        R.id.action_hostFragment_to_detailsFragment,
                        bundleOf("data" to marketData)
                    )
                }
                recyclerView = binding.marketDataRecyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
                sendRefreshedData(marketDataList[0])
            })
        val baseQuoteViewModel: BaseQuoteViewModel by activityViewModels()
        val liveBaseAsset = baseQuoteViewModel.getBaseAsset()
        liveBaseAsset.observe(viewLifecycleOwner, object : Observer<String> {
            override fun onChanged(asset: String) {
                baseAsset = asset
                binding.baseAssetInput.setText(asset)
                liveBaseAsset.removeObserver(this)
            }
        })
        binding.baseAssetSubmit.setOnClickListener {
            baseAsset = binding.baseAssetInput.text.toString()
            if (
                resources
                    .getStringArray(R.array.base_currency_values)
                    .contains(baseAsset)
            ) {

                baseQuoteViewModel.baseAssetInput(
                    baseAsset
                )
            } else {
                Toast.makeText(
                    context,
                    "Unsupported base asset",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun sendRefreshedData(data: MarketData) {
        setFragmentResult(ARG1, bundleOf("data" to data))
    }

    private fun setLink(baseAsset: String, quoteAsset: String) {
        val content = SpannableString("https://www.binance.com/ru/trade/${baseAsset}_${quoteAsset}")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        binding.link.text = content
    }

    fun getQuote(): String {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString("fiat_currency", "")!!
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