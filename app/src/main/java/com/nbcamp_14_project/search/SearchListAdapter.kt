package com.nbcamp_14_project.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbcamp_14_project.databinding.FragmentSearchItemBinding
import com.nbcamp_14_project.home.HomeModel

class SearchListAdapter : ListAdapter<HomeModel, SearchListAdapter.ViewHolder>(
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

    interface ItemClick { //인터페이스
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

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
            )
        )
    }

    class ViewHolder(
        private val binding: FragmentSearchItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeModel) = with(binding) {
            searchTitle.text = item.title
            searchDate.text = item.pubDate?.slice(0..16)
            searchImage.load(item.thumbnail)
        }
    }
}
//    RecyclerView.Adapter<SearchFragmentAdapter.Holder>() {
//
//    val newsList = arrayListOf<SearchModel>()
//
//    // 아이템 전체 삭제
//    fun clearItem() {
//        newsList.clear()
//        notifyDataSetChanged()
//    }
//
//    inner class Holder(val binding: FragmentSearchItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        val title = binding.searchTitle
//        val date = binding.searchDate
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
//        val binding =
//            FragmentSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return Holder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return newsList.size
//    }
//
//    override fun onBindViewHolder(holder: Holder, position: Int) {
//        holder.title.text = newsList[position].title
//        holder.date.text = newsList[position].data.slice(0..16)
//    }
//
//
//}