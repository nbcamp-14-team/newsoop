package com.nbcamp_14_project.debate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.ItemDebateBinding
import com.nbcamp_14_project.databinding.ItemDebatedetailBinding

class DebateDetailListAdapter(private val debatedetailList: List<DebateDetailItem>) : RecyclerView.Adapter<DebateDetailListAdapter.ViewHolder>() {

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }

    var itemClick : ItemClick? = null

    inner class ViewHolder(val binding: ItemDebatedetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val title =binding.tvTitle
        val name = binding.tvName
        val category = binding.tvDate

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDebatedetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {  //클릭이벤트추가부분
            itemClick?.onClick(it, position)
        }
        val debateItem = debatedetailList[position]
        holder.title.text = debateItem.title
        holder.name.text = "이름: ${debateItem.name}"
        holder.category.text = "카테고리: ${debateItem.date}"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return debatedetailList.size
    }
}