package com.nbcamp_14_project.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentSearchBinding
import com.nbcamp_14_project.detail.DetailFragment
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.home.toDetailInfo
import com.nbcamp_14_project.mainpage.MainActivity

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val detailViewModel: DetailViewModel by activityViewModels()

    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this, SearchViewModelFactory()
        )[SearchViewModel::class.java]
    }
    private val adapter by lazy {
        SearchFragmentAdapter(
            onClick = { item ->
                val detailInfo = item.toDetailInfo()
                detailViewModel.setDetailInfo(detailInfo)
                val mainActivity = (activity as MainActivity)
                mainActivity.test()
            }
        )
    }
    private lateinit var adapterManager: LinearLayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
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
                query = tagAdapter.tagList[position]
                viewModel.clearAllItems()
                viewModel.getSearchNews(query, 5, 1)
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
        query = binding.searchInput.text.toString()
        searchBtn.setOnClickListener {
            viewModel.clearAllItems()
            viewModel.getSearchNews(query, 5, 1)
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