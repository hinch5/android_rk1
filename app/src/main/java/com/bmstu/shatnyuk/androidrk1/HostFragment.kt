package com.bmstu.shatnyuk.androidrk1

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model: MarketDataListViewModel by activityViewModels()
        model.getMarketDataList().observe(viewLifecycleOwner, Observer { marketDataList ->
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
    }

    fun sendRefreshedData(data: MarketData) {
        setFragmentResult(ARG1, bundleOf("data" to data))
    }

    companion object {
        @JvmStatic
        fun newInstance() = HostFragment()
    }
}