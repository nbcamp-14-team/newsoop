package com.nbcamp_14_project.newspaper

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbcamp_14_project.databinding.ItemRvNewspaperBinding

class NewsPaperAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<NewspaperModel, NewsPaperAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<NewspaperModel>() {
        override fun areContentsTheSame(oldItem: NewspaperModel, newItem: NewspaperModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: NewspaperModel, newItem: NewspaperModel): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return NewsPaperAdapter.ViewHolder(
            ItemRvNewspaperBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: ItemRvNewspaperBinding,
        private val onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewspaperModel) = with(binding) {
            imgThumbnail.load(item.thumbnail!!)
            imgThumbnail.setOnClickListener {
                Log.d("link", "${item.link}")
                onClick(
                    item.link!!
                )
            }
        }
    }
}
