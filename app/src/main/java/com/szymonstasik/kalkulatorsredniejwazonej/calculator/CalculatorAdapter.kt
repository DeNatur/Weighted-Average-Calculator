package com.szymonstasik.kalkulatorsredniejwazonej.calculator

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.ListItemNoteAndWeightBinding
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Utils


class CalculatorAdapter(
    val changeNoteListener: ChangeNoteListener,
    val changeWeightListener: ChangeWeightListener
): ListAdapter<NoteNWeight,
        CalculatorAdapter.ViewHolder>(NoteNWeightDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) as NoteNWeight
        holder.bind(changeNoteListener, changeWeightListener, item)
    }


    class ViewHolder private constructor(private val binding: ListItemNoteAndWeightBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            changeNoteListener: ChangeNoteListener,
            changeWeightListener: ChangeWeightListener,
            item: NoteNWeight
        ) {
            binding.noteNWeight = item
            binding.executePendingBindings()
            binding.position = adapterPosition
            //Setting adapter for Note Spinner
            val noteNumbers: MutableList<String> = mutableListOf()
            for(i in 0..200){
                var value = Utils.getNoteFromId(i)
                var text = if (Utils.isWhole(value.toDouble()))
                    value.toInt().toString()
                else
                    "${value.toInt()} +"
                noteNumbers.add(text)
            }
            val noteWeights: MutableList<String> = mutableListOf()
            for(i in 1..100){
                noteWeights.add(i.toString())
            }
            var noteAdapter = ArrayAdapter<String>(
                binding.root.context,
                R.layout.dropdown_item,
                noteNumbers
            )

            val spinnerDrawable: Drawable = binding.noteSpinner.background.constantState!!.newDrawable()

            spinnerDrawable.setColorFilter(
                binding.root.context.resources.getColor(R.color.color_title),
                PorterDuff.Mode.SRC_ATOP
            )

            binding.noteSpinner.background = spinnerDrawable
            binding.weightSpinner.background = spinnerDrawable
            noteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.noteSpinner.adapter = noteAdapter
            binding.noteSpinner.setSelection(item.note)

            //Setting adapter for Weight Spinner
            var weightAdapter = ArrayAdapter<String>(
                binding.root.context,
                R.layout.dropdown_item,
                noteWeights
            )
            weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.weightSpinner.adapter = weightAdapter
            binding.weightSpinner.setSelection(item.weight)

            binding.noteSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    changeNoteListener.onChange(adapterPosition, position)
                }
            }

            binding.weightSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    changeWeightListener.onChange(adapterPosition, position)
                }
            }


        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNoteAndWeightBinding.inflate(layoutInflater, parent, false)

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
    class NoteNWeightDiffCallback : DiffUtil.ItemCallback<NoteNWeight>(){
        override fun areItemsTheSame(oldItem: NoteNWeight, newItem: NoteNWeight): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteNWeight, newItem: NoteNWeight): Boolean {
            return oldItem.note == newItem.note &&
                    oldItem.weight == newItem.weight
        }

    }
    class ChangeNoteListener(val changeListener: (position: Int, noteValue: Int) -> Unit) {
        fun onChange(pos: Int, value: Int) {
            changeListener(pos, value)
        }
    }
    class ChangeWeightListener(val changeListener: (position: Int, weightValue: Int) -> Unit) {
        fun onChange(pos: Int, value: Int) {
            changeListener(pos, value)
        }
    }
}