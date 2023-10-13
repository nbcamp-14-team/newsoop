package com.nbcamp_14_project.Main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.FragmentMainBinding
import com.nbcamp_14_project.databinding.ItemRecyclerviewMainFragmentBinding
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent
import retrofit2.Callback
import retrofit2.Response

class MainFragmentAdapter() : ListAdapter<MainFragmentModel, MainFragmentAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<MainFragmentModel>() {
        override fun areContentsTheSame(
            oldItem: MainFragmentModel,
            newItem: MainFragmentModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(
            oldItem: MainFragmentModel,
            newItem: MainFragmentModel
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
        fun bind(item: MainFragmentModel) = with(binding) {
            title.text = item.title
            description.id = item.id

        }
    }

}



