package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver
import com.google.android.gms.ads.AdRequest
import com.kobakei.ratethisapp.RateThisApp
import com.szymonstasik.kalkulatorsredniejwazonej.MainActivity
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDatabase
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.FragmentCalculatorBinding
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.FragmentResultBinding
import com.szymonstasik.kalkulatorsredniejwazonej.history.HistoryViewModel
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Statics
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentResultBinding =  DataBindingUtil.inflate(
            inflater, R.layout.fragment_result, container, false)

        val resultViewModel by viewModel<ResultViewModel>()

        val adapter = ResultNotesAdapter()

        setupUI(binding.parent)

        binding.notesAndWeightsRecycler.adapter = adapter

        binding.resultViewModel = resultViewModel

        binding.lifecycleOwner = this

        binding.tagsRecycler.layoutManager = ChipsLayoutManager.newBuilder(context)
            .setChildGravity(Gravity.LEFT)
            .setScrollingEnabled(false)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .withLastRow(true)
            .build()


        resultViewModel.result.observe(viewLifecycleOwner, Observer {
            binding.resultText.text = getString(R.string.weighted_average_name_and_value, it)
        })

        resultViewModel.weightedAverage.observe(viewLifecycleOwner, Observer {
            if (it != null){
                binding.editTextName.setText(it.name)
                adapter.submitList(it.notes)
            }
        })

        resultViewModel.backPressState.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().popBackStack()
                resultViewModel.donePopBack()
            }
        })

        resultViewModel.navigateToHistory.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().navigate(ResultFragmentDirections.actionResultFragmentToHistoryFragment())
                resultViewModel.onDoneNavigatingToHistory()
            }
        })

        return binding.root
    }

    private fun setupUI(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                try {
                    activity?.let { Utils.hideSoftKeyboard(it) }
                    false
                } catch (e: NullPointerException) {
                    false
                }
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until (view as ViewGroup).childCount) {
                val innerView: View = (view as ViewGroup).getChildAt(i)
                setupUI(innerView)
            }
        }
    }


}
