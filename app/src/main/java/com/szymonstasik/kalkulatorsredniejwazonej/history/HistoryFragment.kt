package com.szymonstasik.kalkulatorsredniejwazonej.history

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverage
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.DialogWeightAverageChooserBinding
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.FragmentHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryFragment : Fragment() {
    private val historyViewModel by viewModel<HistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding: FragmentHistoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_history, container, false)


        val recentAvgAdapter = RecentAvgAdapter()

        val listOfAveragesAdapter = ListOfAveragesAdapter(historyViewModel)

        binding.historyViewModel = historyViewModel
        binding.listOfAveragesRecycler.adapter = listOfAveragesAdapter

        historyViewModel.listOfWeightedAverage.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                binding.noLastAveragesText.visibility = View.VISIBLE
                binding.lastAveragesRecycler.visibility = View.GONE
            }else{
                binding.noLastAveragesText.visibility = View.GONE
                binding.lastAveragesRecycler.visibility = View.VISIBLE
                recentAvgAdapter.submitList(it)
            }
        })

        historyViewModel.listOfWeightedAveragesChooser.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                binding.noListOfAveragesText.visibility = View.VISIBLE
                binding.listOfAveragesRecycler.visibility = View.GONE
            }else{
                binding.noListOfAveragesText.visibility = View.GONE
                binding.listOfAveragesRecycler.visibility = View.VISIBLE
            }
            listOfAveragesAdapter.submitList(it)
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

        binding.lastAveragesRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.lastAveragesRecycler.adapter = recentAvgAdapter

        val itemDecoratorVertical= DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoratorVertical.setDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.divider_line) }!!)
        binding.listOfAveragesRecycler.addItemDecoration(itemDecoratorVertical)

        binding.delete.setOnClickListener {
            showDialog(context = requireContext())
        }
        return binding.root
    }

    private fun showDialog(context: Context){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutInflater = LayoutInflater.from(context)
        val binding: DialogWeightAverageChooserBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_weight_average_chooser, null, false)


        binding.delete.setOnClickListener {
            historyViewModel.onPressDelete()
            dialog.dismiss()
        }

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(binding.root)
        dialog.show()
    }
}
