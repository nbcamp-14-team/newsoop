package com.nbcamp_14_project.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.nbcamp_14_project.databinding.ItemRecyclerviewMainFragmentBinding

class HomeAdapter(
) : ListAdapter<HomeModel, HomeAdapter.ViewHolder>( //뷰페이저2로 수정하기
    object : DiffUtil.ItemCallback<HomeModel>() {
        override fun areContentsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecyclerviewMainFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class ViewHolder(
        private val binding: ItemRecyclerviewMainFragmentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeModel) = with(binding) {
            title.text = item.title
            ivThumbnail.load(item.thumbnail)

        }
    }

}



