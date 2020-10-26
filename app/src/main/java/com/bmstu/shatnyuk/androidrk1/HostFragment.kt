package com.bmstu.shatnyuk.androidrk1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance() = HostFragment()
    }
}