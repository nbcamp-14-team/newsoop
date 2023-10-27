package com.nbcamp_14_project.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.databinding.ItemRvNewsMainBinding
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.detail.DetailViewModel
import java.util.Date

class FavoriteListAdapter(private val onItemClick: (DetailInfo) -> Unit) : ListAdapter<DetailInfo, FavoriteListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<DetailInfo>() {
        // 아이템이 동일한지 비교하기 위한 콜백
        override fun areItemsTheSame(oldItem: DetailInfo, newItem: DetailInfo): Boolean {
            // 아이템의 고유 식별자인 제목을 비교
            return oldItem.title == newItem.title
        }

        // 아이템 내용이 동일한지 비교하기 위한 콜백
        override fun areContentsTheSame(oldItem: DetailInfo, newItem: DetailInfo): Boolean {
            // 아이템 자체를 비교
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 뷰홀더 생성
        return ViewHolder(
            ItemRvNewsMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 뷰홀더에 아이템 데이터 바인딩 및 아이템 클릭 핸들링
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            // 아이템 클릭 시 onItemClick 함수 호출
            onItemClick(item)
        }
    }

    class ViewHolder(private val binding: ItemRvNewsMainBinding) : RecyclerView.ViewHolder(binding.root) {
        // 뷰홀더 클래스 정의

        fun bind(item: DetailInfo) = with(binding) {
            // 아이템 데이터를 뷰에 바인딩
            title.text = item.title // 제목 설정
            ivThumbnail.load(item.thumbnail) // 이미지 불러오기
            val date = Date(item.pubDate)
            val value = date.time?.let { Utils.calculationTime(it) } // 날짜 계산
            pubDate.text = "${item.author} 기자 · $value 작성" // 작성자와 날짜 설정
        }
    }
}
