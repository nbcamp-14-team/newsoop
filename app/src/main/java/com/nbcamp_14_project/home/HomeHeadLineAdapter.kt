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
            ivThumbnail.load(item.thumbnail)
            container.setOnClickListener{
                onClick(
                    item
                )
            }

        }
    }

}


//class HomeHeadLineAdapter(
//) : ListAdapter<HomeModel, HomeHeadLineAdapter.ViewHolder>( //뷰페이저2로 수정하기
//    object : DiffUtil.ItemCallback<HomeModel>() {
//        override fun areContentsTheSame(
//            oldItem: HomeModel,
//            newItem: HomeModel
//        ): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areItemsTheSame(
//            oldItem: HomeModel,
//            newItem: HomeModel
//        ): Boolean {
//            return oldItem == newItem
//        }
//    }) {
//    abstract class ViewHolder(
//        root: View
//    ):RecyclerView.ViewHolder(root){
//        abstract fun bind(item:HomeModel)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
//        return when (viewType) {
//            HomeModel.VIEW_TYPE_HEADLINE -> {
//                HeadLineViewHolder(
//                    ItemRecyclerviewMainFragmentBinding.inflate(
//                        LayoutInflater.from(parent.context),
//                        parent,
//                        false
//                    )
//                )
//            }
//
//            HomeModel.VIEW_TYPE_NEWS -> {
//                NewsViewHolder(
//                    ItemRvNewsMainBinding.inflate(
//                        LayoutInflater.from(parent.context),
//                        parent,
//                        false
//                    )
//                )
//            }
//            else -> throw RuntimeException("뷰타입 지정오류")
//
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        val item = getItem(position)
//        return item.viewType
//    }
//
//    class HeadLineViewHolder(
//        private val binding: ItemRecyclerviewMainFragmentBinding,
//    ) : ViewHolder(binding.root) {
//        override fun bind(item: HomeModel) =with(binding) {
//            if(item is HomeModel){
//                title.text = item.title
//                ivThumbnail.load(item.thumbnail)
//            }
//        }
//
//    }
//
//    class NewsViewHolder(
//        private val binding: ItemRvNewsMainBinding
//    ) : ViewHolder(binding.root){
//        override fun bind(item: HomeModel) =with(binding) {
//            if(item is HomeModel){
//                title.text = item.title
//                ivThumbnail.load(item.thumbnail)
//            }
//        }
//    }
//
//}



