package com.nbcamp_14_project.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.FragmentSearchItemBinding

class SearchFragmentAdapter(private val mContext: Context) :
    RecyclerView.Adapter<SearchFragmentAdapter.Holder>() {

    val newsList = arrayListOf<SearchModel>()

    // 아이템 전체 삭제
    fun clearItem() {
        newsList.clear()
        notifyDataSetChanged()
    }

    inner class Holder(val binding: FragmentSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.searchTitle
        val date = binding.searchDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            FragmentSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = newsList[position].title
        holder.date.text = newsList[position].data.slice(0..16)
    }


}