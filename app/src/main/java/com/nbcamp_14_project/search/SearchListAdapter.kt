package com.nbcamp_14_project.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbcamp_14_project.databinding.FragmentSearchItemBinding
import com.nbcamp_14_project.home.HomeModel

class SearchListAdapter(
    private val onClick: (HomeModel) -> Unit
) : ListAdapter<HomeModel, SearchListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<HomeModel>() {
        override fun areContentsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return oldItem.title == newItem.title
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
            FragmentSearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick

        )
    }

    class ViewHolder(
        private val binding: FragmentSearchItemBinding,
        private val onClick: (HomeModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeModel) = with(binding) {
            searchTitle.text = item.title
            searchDate.text = item.pubDate?.slice(0..16)
            searchImage.load(item.thumbnail)
            cardView.setOnClickListener {
                onClick(
                    item
                )
            }
        }
    }

}