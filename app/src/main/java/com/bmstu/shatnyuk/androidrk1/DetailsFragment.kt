package com.bmstu.shatnyuk.androidrk1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.bmstu.shatnyuk.androidrk1.databinding.FragmentDetailsBinding
import com.bmstu.shatnyuk.androidrk1.model.MarketData
import java.util.*

const val ARG1 = "MARKET_DATA"

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null

    private val binding
        get(): FragmentDetailsBinding {
            return _binding ?: throw Exception("null binding")
        }

    private lateinit var data: MarketData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            handleData(it.getParcelable("data")!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.baseAssetDetail.text = data.baseAsset
        binding.quoteAssetDetail.text = data.quoteAsset
        binding.dateDetail.text = Date(data.closeTime).toString()
        binding.priceDetail.text = data.closePrice
        binding.volumeDetail.text = data.volume
        return binding.root
    }

    private fun handleData(_data: MarketData) {
        data = _data
    }

    companion object {
        @JvmStatic
        fun newInstance(data: MarketData) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG1, data)
                }
            }
    }
}