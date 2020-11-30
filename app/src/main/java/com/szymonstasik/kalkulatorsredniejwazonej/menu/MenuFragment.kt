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
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDatabase
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMenuBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_menu, container, false)

        val viewModelFactory = MenuViewModelFactory()

        val menuViewModel = ViewModelProvider(this, viewModelFactory)[MenuViewModel::class.java]

        binding.menuViewModel = menuViewModel

        binding.lifecycleOwner = this

        menuViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToHistoryFragment())
                menuViewModel.doneHomeNavigating()
            }
        })

        return binding.root
    }

}
