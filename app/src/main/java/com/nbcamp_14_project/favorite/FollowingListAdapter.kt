package com.nbcamp_14_project.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.databinding.ItemRvNewsMainBinding
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.home.HomeModel
import java.util.Date

class FollowingListAdapter(private val onItemClick: (HomeModel) -> Unit) : ListAdapter<HomeModel, FollowingListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<HomeModel>() {
        // 아이템이 동일한지 비교하기 위한 콜백
        override fun areItemsTheSame(oldItem: HomeModel, newItem: HomeModel): Boolean {
            // 아이템의 고유 식별자인 제목을 비교
            return oldItem.title == newItem.title
        }

        // 아이템 내용이 동일한지 비교하기 위한 콜백
        override fun areContentsTheSame(oldItem: HomeModel, newItem: HomeModel): Boolean {
            // 아이템 자체를 비교
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 뷰홀더 생성
        return ViewHolder(
            ItemRvNewsMainBinding.inflate(LayoutInflater.from(parent.context), parent, false),onItemClick
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

    class ViewHolder(private val binding: ItemRvNewsMainBinding, private val onItemClick: (HomeModel) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        // 뷰홀더 클래스 정의

        fun bind(item: HomeModel) = with(binding) {
            // 아이템 데이터를 뷰에 바인딩
            title.text = item.title
            ivThumbnail.load(item.thumbnail)
            val value = item.pubDate?.time?.let { Utils.calculationTime(it) } ?: return
            pubDate.text = item.author + " 기자" + " · " + value + " 작성"


            container.setOnClickListener {
                onItemClick(
                    item
                )
            }
        }
    }
}
