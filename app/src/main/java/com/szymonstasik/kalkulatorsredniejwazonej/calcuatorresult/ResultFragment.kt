package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.AverageTag
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.DialogChooseTagsBinding
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.FragmentResultBinding
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultFragment : Fragment() {
    private val resultViewModel by viewModel<ResultViewModel>()

    private val tagsAdapter = TagsChosenAdapter(list = ArrayList())

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
                Log.d("chosen", "navigate")
                val manager: ReviewManager = ReviewManagerFactory.create(requireActivity())
                val request: Task<ReviewInfo> = manager.requestReviewFlow()
                request.addOnCompleteListener { task ->
                    try {
                        if (task.isSuccessful()) {
                            // We can get the ReviewInfo object
                            val reviewInfo: ReviewInfo = task.getResult()
                            val flow: Task<Void> =
                                manager.launchReviewFlow(requireActivity(), reviewInfo)
                            flow.addOnCompleteListener { task2 ->
                                // The flow has finished. The API does not indicate whether the user
                                // reviewed or not, or even whether the review dialog was shown. Thus, no
                                // matter the result, we continue our app flow.
                                findNavController().navigate(ResultFragmentDirections.actionResultFragmentToHistoryFragment())
                                resultViewModel.onDoneNavigatingToHistory()                            }
                        } else {
                            // There was some problem, continue regardless of the result.
                            findNavController().navigate(ResultFragmentDirections.actionResultFragmentToHistoryFragment())
                            resultViewModel.onDoneNavigatingToHistory()                        }
                    } catch (ex: Exception) {
                        findNavController().navigate(ResultFragmentDirections.actionResultFragmentToHistoryFragment())
                        resultViewModel.onDoneNavigatingToHistory()
                    }
                }


            }
        })

        binding.save.setOnClickListener {
            resultViewModel.onPressSave(binding.editTextName.text.toString())
        }

        binding.notSave.setOnClickListener {
            resultViewModel.onPressNotSave()
        }

        binding.tagButton.setOnClickListener {
            context?.let { it1 -> showTagsDialog(context = it1) }
        }


        binding.tagsRecycler.adapter = tagsAdapter

        binding.tagsRecycler.layoutManager = ChipsLayoutManager.newBuilder(context)
            .setChildGravity(Gravity.LEFT)
            .setScrollingEnabled(false)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .withLastRow(true)
            .build()

        val itemDecoratorHorizontal = DividerItemDecoration(
            context,
            DividerItemDecoration.HORIZONTAL
        )
        itemDecoratorHorizontal.setDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.divider
            )
        }!!)
        val itemDecoratorVertical= DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoratorHorizontal.setDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.divider
            )
        }!!)

        binding.tagsRecycler.addItemDecoration(
            itemDecoratorHorizontal
        )
        binding.tagsRecycler.addItemDecoration(itemDecoratorVertical)

        setTagsRecyclerObserver()

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
        val adapter = TagsAdapter(resultViewModel = resultViewModel, list = ArrayList())

        binding.tagsRecycler.adapter = adapter

        binding.tagsRecycler.layoutManager = ChipsLayoutManager.newBuilder(context)
            .setChildGravity(Gravity.LEFT)
            .setScrollingEnabled(false)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .withLastRow(true)
            .build()

        val itemDecoratorHorizontal = DividerItemDecoration(
            context,
            DividerItemDecoration.HORIZONTAL
        )
        itemDecoratorHorizontal.setDrawable(context.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.divider
            )
        }!!)
        val itemDecoratorVertical= DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoratorHorizontal.setDrawable(context.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.divider
            )
        }!!)

        binding.tagsRecycler.addItemDecoration(
            itemDecoratorHorizontal
        )
        binding.tagsRecycler.addItemDecoration(itemDecoratorVertical)

        resultViewModel.allAverageTags.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.setList(it as ArrayList<TagChooser>)
            }
        })

        binding.fabButton.setOnClickListener {
            resultViewModel.addTag(binding.editTextName.text.toString())
        }

        binding.apply.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(binding.root)
        dialog.show()
    }

    private fun setOnClickListeners(binding: DialogChooseTagsBinding){
        binding.circlePink.setOnClickListener { resultViewModel.setChosenCircle(
            binding.circlePink,
            R.color.pink
        ) }
        binding.circleAutomn.setOnClickListener { resultViewModel.setChosenCircle(
            binding.circleAutomn,
            R.color.automn
        ) }
        binding.circleText.setOnClickListener { resultViewModel.setChosenCircle(
            binding.circleText,
            R.color.color_text
        ) }
        binding.circleViolet.setOnClickListener { resultViewModel.setChosenCircle(
            binding.circleViolet,
            R.color.violet
        ) }
        binding.circleRed.setOnClickListener { resultViewModel.setChosenCircle(
            binding.circleRed,
            R.color.red
        ) }
        binding.circleBlack.setOnClickListener { resultViewModel.setChosenCircle(
            binding.circleBlack,
            R.color.black
        ) }
        binding.circleGreen.setOnClickListener { resultViewModel.setChosenCircle(
            binding.circleGreen,
            R.color.green_light
        ) }
        binding.circleYellow.setOnClickListener { resultViewModel.setChosenCircle(
            binding.circleYellow,
            R.color.yellow
        ) }
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


    private fun setTagsRecyclerObserver(){
        resultViewModel.allAverageTags.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val tmpArray = ArrayList<AverageTag>()
                for (avg in it) {
                    if (avg.chosen) {
                        tmpArray.add(avg.averageTag)
                    }
                }
                Log.d("averageTags", "submitting: " + tmpArray.size)
                tagsAdapter.setList(tmpArray)
            }
        })
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
