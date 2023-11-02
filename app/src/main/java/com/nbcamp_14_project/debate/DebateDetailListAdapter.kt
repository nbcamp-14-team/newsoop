package com.nbcamp_14_project.debate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.ItemDebateBinding
import com.nbcamp_14_project.databinding.ItemDebatedetailBinding

class DebateDetailListAdapter(private val debatedetailList: List<DebateDetailItem>) : RecyclerView.Adapter<DebateDetailListAdapter.ViewHolder>() {

    interface ItemClick {
        fun onDeleteCommentClick(position: Int)
    }

    var itemClick : ItemClick? = null

    inner class ViewHolder(val binding: ItemDebatedetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTitle
        val name = binding.tvName
        val date = binding.tvDate
        val deleteButton = binding.btnDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDebatedetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
        }

        val debateDetailItem = debatedetailList[position]
        holder.title.text = debateDetailItem.text
        holder.name.text = "사용자: ${debateDetailItem.user}"
        holder.date.text = "날짜: ${debateDetailItem.date}"

        holder.deleteButton.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClick?.onDeleteCommentClick(position)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return debatedetailList.size
    }
}
