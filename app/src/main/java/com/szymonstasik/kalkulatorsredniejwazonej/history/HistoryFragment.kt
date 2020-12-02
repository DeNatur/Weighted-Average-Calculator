package com.szymonstasik.kalkulatorsredniejwazonej.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.FragmentHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding: FragmentHistoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_history, container, false)

        val historyViewModel by viewModel<HistoryViewModel>()

        val adapter = HistoryWeightedAverageAdapter(historyViewModel)


        binding.historyViewModel = historyViewModel


        historyViewModel.listOfWeightedAverage.observe(viewLifecycleOwner, Observer {

        })

        historyViewModel.backPressState.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController().popBackStack()
                historyViewModel.donePopBack()
            }
        })

        historyViewModel.navigateToCalculator.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().navigate(HistoryFragmentDirections.actionHistoryFragmentToCalculatorFragment())
                historyViewModel.doneNavigationToCalculator()
            }
        })

        return binding.root
    }
}
