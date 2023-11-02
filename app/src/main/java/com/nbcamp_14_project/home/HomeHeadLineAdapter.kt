package com.nbcamp_14_project.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbcamp_14_project.databinding.ItemRecyclerviewMainFragmentBinding

class HomeHeadLineAdapter(
    private val onClick :(HomeModel) -> Unit
) : ListAdapter<HomeModel, HomeHeadLineAdapter.ViewHolder>(
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
            ),
            onClick
        )
    }

    class ViewHolder(
        private val binding: ItemRecyclerviewMainFragmentBinding,
        private val onClick :(HomeModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeModel) = with(binding) {
            title.text = item.title
            description.text = item.description
            ivThumbnail.load(item.thumbnail)
            container.setOnClickListener{
                onClick(
                    item
                )
            }
        }
    }

}



