package com.nbcamp_14_project.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbcamp_14_project.databinding.ItemRecyclerviewMainFragmentBinding
import com.nbcamp_14_project.databinding.ItemRvNewsMainBinding

class HomeNewsAdapter(
    private val onClick: (HomeModel) -> Unit
) : ListAdapter<HomeModel, HomeNewsAdapter.ViewHolder>(
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
            ItemRvNewsMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick
        )
    }

    class ViewHolder(
        private val binding: ItemRvNewsMainBinding,
        private val onClick: (HomeModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeModel) = with(binding) {
            title.text = item.title
            ivThumbnail.load(item.thumbnail)
            author.text = item.author
            pubDate.text = item.pubDate
            container.setOnClickListener {
                onClick(
                    item
                )
            }
        }

    }
}
