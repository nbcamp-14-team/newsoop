package com.nbcamp_14_project.debate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.ItemDebateBinding
import com.nbcamp_14_project.databinding.ItemDebatedetail2Binding
import com.nbcamp_14_project.databinding.ItemDebatedetailBinding

class DebateDetailListAdapter(private val debatedetailList: List<DebateDetailItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        // position에 해당하는 DebateDetailItem 객체의 viewType을 반환
        return debatedetailList[position].commentType
    }

    interface ItemClick {
        fun onDeleteCommentClick(position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Comment.TYPE_AGREE -> {
                // 찬성 뷰 홀더 생성
                val binding = ItemDebatedetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AgreeViewHolder(binding)
            }
            Comment.TYPE_OPPOSE -> {
                // 반대 뷰 홀더 생성
                val binding = ItemDebatedetail2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                OpposeViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val comment = debatedetailList[position]

        when (holder) {
            is AgreeViewHolder -> {
                // 찬성 뷰 홀더에서 뷰 바인딩 및 데이터 설정
                holder.binding.tvTitle.text = comment.text
                holder.binding.tvName.text = "${comment.user}"
                holder.binding.tvDate.text = "${comment.date}"
            }
            is OpposeViewHolder -> {
                // 반대 뷰 홀더에서 뷰 바인딩 및 데이터 설정
                holder.binding.tvTitle2.text = comment.text
                holder.binding.tvName2.text = "${comment.user}"
                holder.binding.tvDate2.text = "${comment.date}"
            }
        }

        holder.itemView.setOnClickListener {
            // 클릭 이벤트 처리
        }

        holder.itemView.findViewById<View>(R.id.btn_delete)?.setOnClickListener {
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

    // 찬성 뷰 홀더
    class AgreeViewHolder(val binding: ItemDebatedetailBinding) : RecyclerView.ViewHolder(binding.root)

    // 반대 뷰 홀더
    class OpposeViewHolder(val binding: ItemDebatedetail2Binding) : RecyclerView.ViewHolder(binding.root)
}
