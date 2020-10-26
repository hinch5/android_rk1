package com.bmstu.shatnyuk.androidrk1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.bmstu.shatnyuk.androidrk1.model.MarketData

const val ARG1 = "MARKET_DATA"

class DetailsFragment : Fragment() {
    private lateinit var data: MarketData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            handleData(it.getParcelable("data")!!)
        }
        setFragmentResultListener(ARG1) { _, bundle ->
            handleData(bundle.getParcelable("data")!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
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