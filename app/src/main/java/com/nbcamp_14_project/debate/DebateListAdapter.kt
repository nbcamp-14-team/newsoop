// DebateListAdapter.kt
package com.nbcamp_14_project.debate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.ItemDebateBinding

class DebateListAdapter() : RecyclerView.Adapter<DebateListAdapter.ViewHolder>() {
    private val debateList: MutableList<DebateItem> = mutableListOf()
    interface ItemClick {
        fun onClick(view: View, position: Int, title: String)
        fun onDeleteClick(position: Int)
    }

    var itemClick: ItemClick? = null

    inner class ViewHolder(val binding: ItemDebateBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTitle
        val agree = binding.tvAgree
        val opposite = binding.tvOpposite
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
        holder.agree.text = "찬성 : ${debateItem.agreecontext}"
        holder.opposite.text = "반대 : ${debateItem.oppositecontext}"
        holder.name.text = "작성자 : ${debateItem.name}"

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
    fun setDebateList(newDebateList: List<DebateItem>) {
        debateList.clear()
        debateList.addAll(newDebateList)
        notifyDataSetChanged()
    }




}
