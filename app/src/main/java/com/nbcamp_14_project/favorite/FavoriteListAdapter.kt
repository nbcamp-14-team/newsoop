package com.nbcamp_14_project.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.databinding.ItemRvNewsMainBinding
import com.nbcamp_14_project.detail.DetailInfo

class FavoriteListAdapter : ListAdapter<DetailInfo, FavoriteListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<DetailInfo>() {
        override fun areItemsTheSame(
            oldItem: DetailInfo, newItem: DetailInfo
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: DetailInfo,
            newItem: DetailInfo
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteListAdapter.ViewHolder {
        return ViewHolder(
            ItemRvNewsMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: ItemRvNewsMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetailInfo) = with(binding) {

            title.text = item.title
            ivThumbnail.load(item.thumbnail)
            author.text = item.author
            pubDate.text = item.pubDate

        }
    }
}