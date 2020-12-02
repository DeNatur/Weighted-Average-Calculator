package com.szymonstasik.kalkulatorsredniejwazonej.history

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverage
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.DialogWeightAverageChooserBinding
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.ListItemWeightedAverageBinding

class HistoryWeightedAverageAdapter(private val historyViewModel: HistoryViewModel)
    : ListAdapter<WeightedAverage, HistoryWeightedAverageAdapter.ViewHolder>
    (WeightedAverageDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) as WeightedAverage
        holder.bind(item, historyViewModel)
    }

    class ViewHolder private constructor(private val binding: ListItemWeightedAverageBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeightedAverage, historyViewModel: HistoryViewModel) {
            binding.weightedAverage = item
            binding.root.context
            binding.parent.setOnClickListener {
                showDialog(binding.root.context, item, historyViewModel)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWeightedAverageBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

        private fun showDialog(context: Context, item: WeightedAverage, historyViewModel: HistoryViewModel){
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val layoutInflater = LayoutInflater.from(context)
            val binding: DialogWeightAverageChooserBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.dialog_weight_average_chooser, null, false)

            binding.historyViewModel = historyViewModel

            binding.deleteNote.setOnClickListener {
//                historyViewModel.onDeleteClick(item)
                dialog.dismiss()
            }

            binding.editNote.setOnClickListener {
//                historyViewModel.onEditClick(item.id)
                dialog.dismiss()
            }
            dialog.setContentView(binding.root)
            dialog.show()
        }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     *
     * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
     * list that's been passed to `submitList`.
     */
    class WeightedAverageDiffCallback : DiffUtil.ItemCallback<WeightedAverage>(){
        override fun areItemsTheSame(oldItem: WeightedAverage, newItem: WeightedAverage): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeightedAverage, newItem: WeightedAverage): Boolean {
            var same = true
            for (position in oldItem.notes.indices){
                if(position < oldItem.notes.size && position < newItem.notes.size){
                    if (oldItem.notes[position] != newItem.notes[position]) {
                        same = false
                        break
                    }
                }else{
                    same = false
                    break
                }

            }
            return same &&
                    oldItem.timeAddedMilli == newItem.timeAddedMilli &&
                    oldItem.name == newItem.name
        }
    }

}