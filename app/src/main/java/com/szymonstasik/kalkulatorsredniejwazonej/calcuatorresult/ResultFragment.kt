package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.DialogChooseTagsBinding
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.FragmentResultBinding
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultFragment : Fragment() {
    val resultViewModel by viewModel<ResultViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentResultBinding =  DataBindingUtil.inflate(
            inflater, R.layout.fragment_result, container, false
        )


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
            if (it != null) {
                binding.editTextName.setText(it.name)
                adapter.submitList(it.notes)
            }
        })

        resultViewModel.backPressState.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().popBackStack()
                resultViewModel.donePopBack()
            }
        })

        resultViewModel.navigateToHistory.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(ResultFragmentDirections.actionResultFragmentToHistoryFragment())
                resultViewModel.onDoneNavigatingToHistory()
            }
        })

        binding.tagButton.setOnClickListener {
            context?.let { it1 -> showTagsDialog(it1) }
        }

        return binding.root
    }

    private fun showTagsDialog(context: Context){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutInflater = LayoutInflater.from(context)
        val binding: DialogChooseTagsBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_choose_tags, null, false)
        resultViewModel.chosenCircle.observe(viewLifecycleOwner, Observer {
            setAllSolid(binding)
            setSolidColor(it.circleImageView, it.colorId)
        })
        setAllStroke(binding)
        setOnClickListeners(binding)
        resultViewModel.setChosenCircle(binding.circlePink, R.color.pink)

        dialog.setContentView(binding.root)
        dialog.show()
    }

    private fun setOnClickListeners(binding: DialogChooseTagsBinding){
        binding.circlePink.setOnClickListener { resultViewModel.setChosenCircle(binding.circlePink, R.color.pink) }
        binding.circleAutomn.setOnClickListener { resultViewModel.setChosenCircle(binding.circleAutomn, R.color.automn) }
        binding.circleText.setOnClickListener { resultViewModel.setChosenCircle(binding.circleText, R.color.color_text) }
        binding.circleViolet.setOnClickListener { resultViewModel.setChosenCircle(binding.circleViolet, R.color.violet) }
        binding.circleRed.setOnClickListener { resultViewModel.setChosenCircle(binding.circleRed, R.color.red) }
        binding.circleBlack.setOnClickListener { resultViewModel.setChosenCircle(binding.circleBlack, R.color.black) }
        binding.circleGreen.setOnClickListener { resultViewModel.setChosenCircle(binding.circleGreen, R.color.green_light) }
        binding.circleYellow.setOnClickListener { resultViewModel.setChosenCircle(binding.circleYellow, R.color.yellow) }
    }

    private fun setAllStroke(binding: DialogChooseTagsBinding) {
        setStrokeColor(binding.circlePink, R.color.pink)
        setStrokeColor(binding.circleAutomn, R.color.automn)
        setStrokeColor(binding.circleText, R.color.color_text)
        setStrokeColor(binding.circleViolet, R.color.violet)
        setStrokeColor(binding.circleRed, R.color.red)
        setStrokeColor(binding.circleBlack, R.color.black)
        setStrokeColor(binding.circleGreen, R.color.green_light)
        setStrokeColor(binding.circleYellow, R.color.yellow)
    }

    private fun setAllSolid(binding: DialogChooseTagsBinding) {
        setSolidColor(binding.circlePink, R.color.bg_start)
        setSolidColor(binding.circleAutomn, R.color.bg_start)
        setSolidColor(binding.circleText, R.color.bg_start)
        setSolidColor(binding.circleViolet, R.color.bg_start)
        setSolidColor(binding.circleRed, R.color.bg_start)
        setSolidColor(binding.circleBlack, R.color.bg_start)
        setSolidColor(binding.circleGreen, R.color.bg_start)
        setSolidColor(binding.circleYellow, R.color.bg_start)
    }

    private fun setSolidColor(imageView: ImageView, colorId: Int){
        val drawable = imageView.background as GradientDrawable
        drawable.setColor(resources.getColor(colorId))

    }

    private fun setStrokeColor(imageView: ImageView, colorId: Int) {
        val drawable = imageView.background as GradientDrawable
        drawable.setStroke(6, resources.getColor(colorId))
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
