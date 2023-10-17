package com.nbcamp_14_project.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this, SearchFragmentModelFactory()
        )[SearchViewModel::class.java]
    }
    private val adapter by lazy { SearchFragmentAdapter() }
    private lateinit var adapterManager: LinearLayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private var query = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initView()
        initViewModel()
        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.searchRecyclerView.canScrollVertically(1)) {
                    //viewModel.headLineNews(query)
                }
            }
        })
        return binding.root
    }

    fun initView() = with(binding) {
        adapterManager = LinearLayoutManager(requireContext())
        searchRecyclerView.layoutManager = adapterManager
        searchRecyclerView.adapter = adapter
        searchBtn.setOnClickListener {
            query = binding.searchInput.text.toString()
            viewModel.getSearchNews(query)

            //키보드 내리는 기능
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchInput.windowToken, 0)
        }
    }

    fun initViewModel() {
        with(viewModel) {
            list.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }
}