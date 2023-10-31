package com.nbcamp_14_project.debate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.ItemDebateBinding

class DebateListAdapter(private val debateList: List<DebateItem>) : RecyclerView.Adapter<DebateListAdapter.ViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int, title: String)
        fun onDeleteClick(position: Int)
    }

    var itemClick: ItemClick? = null

    inner class ViewHolder(val binding: ItemDebateBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTitle
        val name = binding.tvName
        val deleteButton = binding.btnDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDebateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position, debateList[position].title)
        }
        val debateItem = debateList[position]
        holder.title.text = debateItem.title
        holder.name.text = "이름: ${debateItem.name}"


        holder.deleteButton.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClick?.onDeleteClick(position)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return debateList.size
    }
}
