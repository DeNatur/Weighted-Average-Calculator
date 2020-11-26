package com.szymonstasik.kalkulatorsredniejwazonej.menu

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
import com.kobakei.ratethisapp.RateThisApp
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDatabase
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMenuBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_menu, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = WeightedAverageDatabase.getInstance(application).weightedAverageDao

        val viewModelFactory = MenuViewModelFactory(dataSource)

        val menuViewModel = ViewModelProvider(this, viewModelFactory)[MenuViewModel::class.java]

        binding.menuViewModel = menuViewModel

        binding.lifecycleOwner = this

        binding.rippleBackground.startRippleAnimation();

        menuViewModel.navigateToCalculator.observe(viewLifecycleOwner, Observer {
            if (it != null){
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToCalculatorFragment(it))
                menuViewModel.doneCalculatorNavigating()
            }
        })

        binding.historyButton.setOnClickListener { onHistory() }



        return binding.root
    }


    private fun onHistory(){
        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToHistoryFragment())
    }

}
