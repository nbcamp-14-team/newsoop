// DebateDetailListAdapter.kt
package com.nbcamp_14_project.debate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.nbcamp_14_project.databinding.ItemDebatedetail2Binding
import com.nbcamp_14_project.databinding.ItemDebatedetailBinding

// RecyclerView.Adapter를 상속받은 DebateDetailListAdapter 클래스 정의
class DebateDetailListAdapter(private val debatedetailList: List<DebateDetailItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // getItemViewType 함수 오버라이드 - 아이템 뷰 타입 반환
    override fun getItemViewType(position: Int): Int {
        // position에 해당하는 DebateDetailItem 객체의 viewType을 반환
        return debatedetailList[position].commentType
    }

    // 클릭 이벤트 처리를 위한 인터페이스 정의
    interface ItemClick {
        fun onDeleteCommentClick(position: Int)
    }

    // ItemClick 인터페이스 타입의 변수 선언
    var itemClick: ItemClick? = null

    // onCreateViewHolder 함수 오버라이드 - 뷰 홀더 객체 생성
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

    // onBindViewHolder 함수 오버라이드 - 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 현재 위치의 토론 댓글 아이템 가져오기
        val comment = debatedetailList[position]

        // 뷰 홀더의 타입에 따라 데이터 바인딩 처리
        when (holder) {
            is AgreeViewHolder -> {
                // 찬성 뷰 홀더에서 뷰 바인딩 및 데이터 설정
                holder.binding.tvTitle.text = comment.text
                holder.binding.tvName.text = "${comment.user}"
                holder.binding.tvDate.text = "${comment.date}"

                // 본인 댓글인 경우 삭제 버튼 표시, 아닌 경우 숨김
                if (isCommentOwner(comment)) {
                    holder.binding.btnDelete.visibility = View.VISIBLE
                    // 삭제 버튼 클릭 시 onDeleteCommentClick 콜백 호출
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

                // 본인 댓글인 경우 삭제 버튼 표시, 아닌 경우 숨김
                if (isCommentOwner(comment)) {
                    holder.binding.btnDelete.visibility = View.VISIBLE
                    // 삭제 버튼 클릭 시 onDeleteCommentClick 콜백 호출
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

        // 아이템 뷰 클릭 시 처리할 이벤트 정의
        holder.itemView.setOnClickListener {
            // 클릭 이벤트 처리
        }
    }

    // getItemId 함수 오버라이드 - 아이템의 고유 식별자 반환
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // getItemCount 함수 오버라이드 - 아이템 수 반환
    override fun getItemCount(): Int {
        return debatedetailList.size
    }

    // 본인 댓글 여부 확인 함수
    private fun isCommentOwner(comment: DebateDetailItem): Boolean {
        // 여기에서 comment.userUID와 현재 사용자의 UID를 비교하여 본인 댓글인지 판단
        val currentUserUID = getCurrentUserUID() // 현재 사용자의 UID를 가져오는 함수를 호출

        return comment.userUID == currentUserUID
    }

    // 현재 사용자의 UID를 가져오는 함수
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
