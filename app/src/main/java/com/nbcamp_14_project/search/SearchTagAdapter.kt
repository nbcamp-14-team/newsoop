package com.nbcamp_14_project.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.FragmentSearchTagBinding

class SearchTagAdapter : RecyclerView.Adapter<SearchTagAdapter.Holder>() {

    val tagList = arrayListOf<String>("정치", "경제", "사회", "생활", "문화", "IT", "과학", "세계")

    inner class Holder(val binding: FragmentSearchTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tagBtn = binding.searchTagBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            FragmentSearchTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tagBtn.text = tagList[position]
    }
}