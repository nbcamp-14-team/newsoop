package com.nbcamp_14_project.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.FragmentSearchTagBinding

class SearchTagAdapter : RecyclerView.Adapter<SearchTagAdapter.ViewHolder>() {

    val tagList = arrayListOf<String>("정치", "경제", "사회", "생활", "문화", "IT", "과학", "세계")

    class ViewHolder(
        private val binding: FragmentSearchTagBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        val btn = binding.searchTagBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentSearchTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btn.text = tagList[position]
        holder.btn.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener: OnItemClickListener
}