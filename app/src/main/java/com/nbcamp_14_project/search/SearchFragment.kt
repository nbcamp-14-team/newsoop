package com.nbcamp_14_project.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this, SearchViewModelFactory()
        )[SearchViewModel::class.java]
    }
    private val adapter by lazy { SearchListAdapter() }
    private val tagAdapter by lazy { SearchTagAdapter() }

    private var query = ""
//    private var countStart = 11

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initView()
        initViewModel()
        // TODO :  retrofit2.HttpException: HTTP 400  fix
//        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!binding.searchRecyclerView.canScrollVertically(1)) {
//                    if (countStart <= 100) {
//                        viewModel.getSearchNews(query, 10, countStart)
//                        countStart += 10
//                        Log.d("TAG", "count : ${countStart}")
//                    } else {
//                        Toast.makeText(requireContext(), "마지막", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        })
        tagAdapter.setItemClickListener(object : SearchTagAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Log.d("TAG", "tag Click : $position")
                val tag = tagAdapter.tagList[position]
                viewModel.getSearchNews(tag, 10, 1)
            }
        })
        return binding.root
    }

    private fun initView() = with(binding) {
        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchRecyclerView.adapter = adapter
        searchTagFrame.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        searchTagFrame.adapter = tagAdapter
        searchInput.setOnEditorActionListener { _, i, _ ->
            var handled = false
            if (i == EditorInfo.IME_ACTION_DONE) {
                Log.d("TAG", "enterEvent")
                searchBtn.performClick()
                handled = true
            }
            handled
        }
        searchBtn.setOnClickListener {
            query = binding.searchInput.text.toString()
            viewModel.getSearchNews(query, 10, 1)

            //키보드 내리는 기능
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchInput.windowToken, 0)
        }
    }

    private fun initViewModel() {
        with(viewModel) {
            searchResultList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }
}