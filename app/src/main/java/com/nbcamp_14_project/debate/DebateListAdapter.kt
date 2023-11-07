// DebateListAdapter.kt
package com.nbcamp_14_project.debate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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
        val debateItem = debateList[position]

        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid

        // 현재 사용자가 해당 토론 주제를 작성한 사용자일 경우에만 삭제 버튼 표시
        if (currentUserId == debateItem.userUID) {
            holder.deleteButton.visibility = View.VISIBLE
            holder.deleteButton.setOnClickListener {
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClick?.onDeleteClick(position)
                }
            }
        } else {
            // 다른 사용자가 작성한 토론 주제인 경우 삭제 버튼 숨김
            holder.deleteButton.visibility = View.GONE
        }

        // 나머지 코드는 이전과 동일
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position, debateItem.title)
        }
        holder.title.text = debateItem.title
        holder.agree.text = "찬성 : ${debateItem.agreecontext}"
        holder.opposite.text = "반대 : ${debateItem.oppositecontext}"
        holder.name.text = "${debateItem.name}"
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
