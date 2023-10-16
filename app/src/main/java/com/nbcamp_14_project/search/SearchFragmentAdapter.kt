package com.nbcamp_14_project.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.FragmentSearchItemBinding
import com.nbcamp_14_project.home.HomeModel

class SearchFragmentAdapter(val newsList: List<HomeModel>) :
    RecyclerView.Adapter<SearchFragmentAdapter.Holder>() {

    inner class Holder(val binding: FragmentSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.itemTitle
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
    }
}