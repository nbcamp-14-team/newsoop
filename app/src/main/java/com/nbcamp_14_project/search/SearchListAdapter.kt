package com.nbcamp_14_project.search

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.databinding.FragmentSearchItemBinding
import com.nbcamp_14_project.home.HomeModel
import com.nbcamp_14_project.ui.login.LoginActivity

class SearchListAdapter(
    private val onClick: (HomeModel) -> Unit,
    private val onSwitch: (HomeModel) -> Unit
) : ListAdapter<HomeModel, SearchListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<HomeModel>() {
        override fun areContentsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return oldItem.title == newItem.title
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
            FragmentSearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick,
            onSwitch

        )
    }


    class ViewHolder(
        private val binding: FragmentSearchItemBinding,
        private val onClick: (HomeModel) -> Unit,
        private val onSwitch: (HomeModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeModel) = with(binding) {
            searchTitle.text = item.title
            searchDate.text = item.pubDate?.time?.let { Utils.calculationTime(it) } ?: ""
            searchImage.clipToOutline = true
            searchImage.load(item.thumbnail)
            searchSwitch.isChecked = item.isLike!!
            cardView.setOnClickListener {
                onClick(
                    item
                )
            }
            searchSwitch.setOnClickListener {
                val searchFragment = SearchFragment.newInstance()
                val user = searchFragment.user
                Log.d("searchuser","$user")
                if(user == null){
                        searchSwitch.isChecked = !searchSwitch.isChecked
                    onSwitch(
                        item.copy(
                            isLike = searchSwitch.isChecked
                        )
                    )

                }else if (item.isLike != searchSwitch.isChecked) {// Item의 isLike 상태와 스위치의 상태가 다르면 실행

                    item.isLike = searchSwitch.isChecked
                    Log.d("ischecked?", "${item.isLike}")
                    onSwitch(
                        item.copy(
                            isLike = searchSwitch.isChecked
                        )
                    )

                }

            }
        }
    }
}