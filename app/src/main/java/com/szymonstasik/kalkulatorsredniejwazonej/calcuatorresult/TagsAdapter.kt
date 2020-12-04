package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.ListItemNoteAndWeightResultBinding

class TagsAdapter:
    ListAdapter<NoteNWeight, ResultNotesAdapter.ViewHolder>(NoteNWeightDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) as NoteNWeight
        holder.bind(item)
    }


    class ViewHolder private constructor(private val binding: ListItemNoteAndWeightResultBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteNWeight) {
            binding.noteNWeight = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNoteAndWeightResultBinding.inflate(layoutInflater, parent, false)

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

    override fun onBindViewHolder(holder: ResultNotesAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}