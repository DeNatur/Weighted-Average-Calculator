package com.szymonstasik.kalkulatorsredniejwazonej.history

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult.TagChooser
import com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult.TagsAdapter
import com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult.TagsChosenAdapter
import com.szymonstasik.kalkulatorsredniejwazonej.database.AverageTag
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverage
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.ItemRecentAveragesBinding
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.ListItemWeightedAverageChooserBinding

class ListOfAveragesAdapter(private val historyViewModel: HistoryViewModel)
    : ListAdapter<WeightedAverageChooser, ListOfAveragesAdapter.ViewHolder>
    (TagChoserDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) as WeightedAverageChooser
        holder.bind(item, historyViewModel)
    }

    class ViewHolder private constructor(private val binding: ListItemWeightedAverageChooserBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeightedAverageChooser, historyViewModel: HistoryViewModel) {
            binding.weightedAverage = item.weightedAverage
            binding.chosen = item.chosen
            val tmpArray = ArrayList<AverageTag>()
            for(avg in item.weightedAverage.tags){
                tmpArray.add(avg)
            }
            binding.checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
                historyViewModel.changeWeightedAverageToChosen(item)
            })
            binding.parent.setOnClickListener {
                historyViewModel.onEditClick(item.weightedAverage)
            }
            if(tmpArray.isNotEmpty()){
                val adapter = TagsChosenAdapter(tmpArray)

                binding.tagsRecycler.adapter = adapter

                binding.tagsRecycler.layoutManager = ChipsLayoutManager.newBuilder(binding.root.context)
                    .setChildGravity(Gravity.LEFT)
                    .setScrollingEnabled(false)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .withLastRow(true)
                    .build()
                val itemDecoratorHorizontal = DividerItemDecoration(binding.root.context, DividerItemDecoration.HORIZONTAL)
                itemDecoratorHorizontal.setDrawable(binding.root.context?.let { ContextCompat.getDrawable(it, R.drawable.divider) }!!)
                val itemDecoratorVertical= DividerItemDecoration(binding.root.context, DividerItemDecoration.VERTICAL)
                itemDecoratorHorizontal.setDrawable(binding.root.context?.let { ContextCompat.getDrawable(it, R.drawable.divider) }!!)

                binding.tagsRecycler.addItemDecoration(itemDecoratorVertical)
                binding.tagsRecycler.addItemDecoration(itemDecoratorHorizontal)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWeightedAverageChooserBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     *
     * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
     * list that's been passed to `submitList`.
     */
    class TagChoserDiffCallback : DiffUtil.ItemCallback<WeightedAverageChooser>(){
        override fun areItemsTheSame(oldItem: WeightedAverageChooser, newItem: WeightedAverageChooser): Boolean {
            return  oldItem.weightedAverage.id == newItem.weightedAverage.id
        }

        override fun areContentsTheSame(oldItem: WeightedAverageChooser, newItem: WeightedAverageChooser): Boolean {
            var sameWeightedAverage = true
            var sameAverageTags = true
            for (position in oldItem.weightedAverage.notes.indices){
                if(position < oldItem.weightedAverage.notes.size && position < newItem.weightedAverage.notes.size){
                    if (oldItem.weightedAverage.notes[position] != newItem.weightedAverage.notes[position]) {
                        sameWeightedAverage = false
                        break
                    }
                }else{
                    sameWeightedAverage = false
                    break
                }
            }
            for(position in oldItem.weightedAverage.tags.indices){
                if(position < oldItem.weightedAverage.tags.size && position < newItem.weightedAverage.tags.size){
                    if (oldItem.weightedAverage.tags[position] != newItem.weightedAverage.tags[position]) {
                        sameAverageTags = false
                        break
                    }
                }else{
                    sameAverageTags = false
                    break
                }
            }

            return sameAverageTags && sameWeightedAverage &&
                    oldItem.weightedAverage.timeAddedMilli == newItem.weightedAverage.timeAddedMilli &&
                    oldItem.weightedAverage.name == newItem.weightedAverage.name
        }
    }

}