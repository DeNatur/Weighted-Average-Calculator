package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.AverageTag
import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.ListItemNoteAndWeightResultBinding
import com.szymonstasik.kalkulatorsredniejwazonej.databinding.ListItemTagDialogBinding

class TagsChosenAdapter(private var list: ArrayList<AverageTag>):
    RecyclerView.Adapter<TagsChosenAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun setList(listToChange: ArrayList<AverageTag>){
        list = listToChange
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }


    class ViewHolder private constructor(private val binding: ListItemTagDialogBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AverageTag) {
            binding.executePendingBindings()
            val drawable = binding.parentView.background as GradientDrawable
            drawable.setStroke(6, binding.root.context.resources.getColor(item.color))
            drawable.setColor(binding.root.context.resources.getColor(R.color.bg_start))
            binding.text.text = item.name
            binding.text.setTextColor(binding.root.context.resources.getColor(R.color.black))
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