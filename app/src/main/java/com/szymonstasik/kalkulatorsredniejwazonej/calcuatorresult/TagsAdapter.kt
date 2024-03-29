package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.ListItemNoteAndWeightResultBinding
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.ListItemTagDialogBinding

class TagsAdapter(private var list: ArrayList<TagChooser>, var resultViewModel: ResultViewModel):
    RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun setList(listToChange: ArrayList<TagChooser>){
        list = listToChange
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, resultViewModel)
    }


    class ViewHolder private constructor(private val binding: ListItemTagDialogBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TagChooser, resultViewModel: ResultViewModel) {
            binding.tagChooser = item
            binding.executePendingBindings()
            binding.parentView.setOnClickListener {
                resultViewModel.changeTagToChosen(item)
            }
            val drawable = binding.parentView.background as GradientDrawable
            drawable.setStroke(6, binding.root.context.resources.getColor(item.averageTag.color))
            if(item.chosen){
                drawable.setColor(binding.root.context.resources.getColor(item.averageTag.color))
                binding.text.setTextColor(binding.root.context.resources.getColor(R.color.white))
            }else{
                drawable.setColor(binding.root.context.resources.getColor(R.color.bg_start))
                binding.text.setTextColor(binding.root.context.resources.getColor(R.color.bg))

            }
            binding.text.text = item.averageTag.name
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTagDialogBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount() = list.size

}