// DebateListAdapter.kt
package com.nbcamp_14_project.debate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.nbcamp_14_project.databinding.ItemDebateBinding

// RecyclerView.Adapter를 상속받은 DebateListAdapter 클래스 정의
class DebateListAdapter() : RecyclerView.Adapter<DebateListAdapter.ViewHolder>() {
    // 토론 목록을 담을 MutableList 생성
    private val debateList: MutableList<DebateItem> = mutableListOf()

    // 클릭 이벤트를 처리할 인터페이스 정의
    interface ItemClick {
        fun onClick(view: View, position: Int, title: String)
        fun onDeleteClick(position: Int)
    }

    // ItemClick 인터페이스 타입의 변수 선언
    var itemClick: ItemClick? = null

    // ViewHolder 클래스 정의
    inner class ViewHolder(val binding: ItemDebateBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTitle
        val agree = binding.tvAgree
        val opposite = binding.tvOpposite
        val name = binding.tvName
        val deleteButton = binding.btnDelete
    }

    // onCreateViewHolder 함수 오버라이드 - 뷰 홀더 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // XML 레이아웃 파일을 사용하여 뷰 홀더 객체 생성
        val binding = ItemDebateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // onBindViewHolder 함수 오버라이드 - 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 현재 위치의 토론 아이템 가져오기
        val debateItem = debateList[position]

        // 현재 사용자 정보 가져오기
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid

        // 현재 사용자가 해당 토론 주제를 작성한 사용자일 경우에만 삭제 버튼 표시
        if (currentUserId == debateItem.userUID) {
            holder.deleteButton.visibility = View.VISIBLE
            // 삭제 버튼 클릭 시 onDeleteClick 콜백 호출
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

        // 아이템 뷰 클릭 시 onClick 콜백 호출
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position, debateItem.title)
        }

        // 토론 아이템의 정보를 뷰에 바인딩
        holder.title.text = debateItem.title
        holder.agree.text = "찬성 : ${debateItem.agreecontext}"
        holder.opposite.text = "반대 : ${debateItem.oppositecontext}"
        holder.name.text = "${debateItem.name}"
    }

    // getItemId 함수 오버라이드 - 아이템의 고유 식별자를 반환
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // getItemCount 함수 오버라이드 - 아이템 수 반환
    override fun getItemCount(): Int {
        return debateList.size
    }

    // setDebateList 함수 - 토론 목록 갱신
    fun setDebateList(newDebateList: List<DebateItem>) {
        debateList.clear()
        debateList.addAll(newDebateList)
        notifyDataSetChanged()
    }
}
