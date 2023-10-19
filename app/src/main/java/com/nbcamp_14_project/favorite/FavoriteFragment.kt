package com.nbcamp_14_project.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nbcamp_14_project.R
import com.nbcamp_14_project.home.Home
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.detail.DetailFragment
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.home.toDetailInfo
import com.nbcamp_14_project.mainpage.MainActivity

class FavoriteFragment: Fragment() {
    companion object{
        fun newInstance() = FavoriteFragment()
    }
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteListAdapter
    private val viewModel: FavoriteViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        adapter = FavoriteListAdapter { item ->

            val detailInfo = item
            detailViewModel.setDetailInfo(detailInfo)
            val mainActivity = (activity as MainActivity)
            mainActivity.test()
        }
        binding.favoriteList.layoutManager = LinearLayoutManager(context)
        binding.favoriteList.adapter = adapter
        viewModel.favoriteList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }


    }

}