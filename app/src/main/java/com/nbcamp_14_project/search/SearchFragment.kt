package com.nbcamp_14_project.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nbcamp_14_project.databinding.FragmentSearchBinding
import com.nbcamp_14_project.home.HomeModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchBtn.setOnClickListener {
            val viewModel = SearchViewModel()
            var newsList = arrayListOf<HomeModel>()
            val query = binding.searchInput.text.toString()
            newsList.clear()
            viewModel.getNewsList(query)
            viewModel.newsListResult.observe(viewLifecycleOwner) {
                binding.searchRecyclerView.adapter = SearchFragmentAdapter(it)
                binding.searchRecyclerView.layoutManager = LinearLayoutManager(mContext)
            }
        }
        return binding.root
    }
}