package com.nbcamp_14_project.debate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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

                // 본인 댓글인 경우 버튼 표시, 아닌 경우 숨김
                if (isCommentOwner(comment)) {
                    holder.binding.btnDelete.visibility = View.VISIBLE
                    holder.binding.btnDelete.setOnClickListener {
                        val position = holder.adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            itemClick?.onDeleteCommentClick(position)
                        }
                    }
                } else {
                    holder.binding.btnDelete.visibility = View.GONE
                }
            }
            is OpposeViewHolder -> {
                // 반대 뷰 홀더에서 뷰 바인딩 및 데이터 설정
                holder.binding.tvTitle2.text = comment.text
                holder.binding.tvName2.text = "${comment.user}"
                holder.binding.tvDate2.text = "${comment.date}"

                // 본인 댓글인 경우 버튼 표시, 아닌 경우 숨김
                if (isCommentOwner(comment)) {
                    holder.binding.btnDelete.visibility = View.VISIBLE
                    holder.binding.btnDelete.setOnClickListener {
                        val position = holder.adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            itemClick?.onDeleteCommentClick(position)
                        }
                    }
                } else {
                    holder.binding.btnDelete.visibility = View.GONE
                }
            }
        }

        holder.itemView.setOnClickListener {
            // 클릭 이벤트 처리
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return debatedetailList.size
    }

    private fun isCommentOwner(comment: DebateDetailItem): Boolean {
        // 여기에서 comment.userUID와 현재 사용자의 UID를 비교하여 본인 댓글인지 판단
        val currentUserUID = getCurrentUserUID() // 현재 사용자의 UID를 가져오는 함수를 호출

        return comment.userUID == currentUserUID
    }

    private fun getCurrentUserUID(): String {
        // Firebase의 현재 사용자 객체를 가져옴
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        // Firebase 사용자 객체의 UID 반환, 만약 사용자가 로그인되어 있지 않다면 빈 문자열 반환
        return firebaseUser?.uid ?: ""
    }


    // 찬성 뷰 홀더
    class AgreeViewHolder(val binding: ItemDebatedetailBinding) : RecyclerView.ViewHolder(binding.root)

    // 반대 뷰 홀더
    class OpposeViewHolder(val binding: ItemDebatedetail2Binding) : RecyclerView.ViewHolder(binding.root)
}
