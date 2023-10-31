package com.nbcamp_14_project.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.databinding.ItemLoadingBinding
import com.nbcamp_14_project.databinding.ItemProfressBarBinding
import com.nbcamp_14_project.databinding.ItemRecyclerviewMainFragmentBinding
import com.nbcamp_14_project.databinding.ItemRvNewsMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

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
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is NewsViewHolder) {
            holder.bind(item)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            VIEW_TYPE_ITEM ->{
                return NewsViewHolder(
                    ItemRvNewsMainBinding.inflate(
                        LayoutInflater.from(parent.context),parent,false
                    ),
                    onClick,
                )
            }
            else ->{
                return LoadingViewHolder(
                    ItemProfressBarBinding.inflate(
                        LayoutInflater.from(parent.context),parent,false
                    )
                )
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when(item.viewType){
            VIEW_TYPE_LOADING -> VIEW_TYPE_LOADING
            VIEW_TYPE_ITEM -> VIEW_TYPE_ITEM
            else -> 3
        }
    }

    abstract class ViewHolder(
        root: View
    ):RecyclerView.ViewHolder(root){
        abstract fun bind(item:HomeModel)
    }

    class NewsViewHolder(
        private val binding: ItemRvNewsMainBinding,
        private val onClick: (HomeModel) -> Unit
    ) : ViewHolder(binding.root){
        override fun bind(item: HomeModel) = with(binding) {
            title.text = item.title
            ivThumbnail.load(item.thumbnail)
            val value = item.pubDate?.time?.let { Utils.calculationTime(it) } ?: return
            pubDate.text = item.author + " 기자" + " · " + value + " 작성"


            container.setOnClickListener {
                onClick(
                    item
                )
            }
        }

    }
    class LoadingViewHolder(
        private val binding : ItemProfressBarBinding
    ):ViewHolder(binding.root){
        override fun bind(item: HomeModel) {

        }
    }
}
