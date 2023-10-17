package com.nbcamp_14_project.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nbcamp_14_project.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private val viewModel = SearchViewModel()

    private lateinit var adapter: SearchFragmentAdapter
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
        setupViews(inflater, container)
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()              // ViewModel 관찰 설정

    }

    fun observeViewModel() {
        viewModel.newsListResult.observe(viewLifecycleOwner) { items ->
            adapter.newsList.addAll(items)
            adapter.notifyDataSetChanged()
        }
    }

    fun setupViews(inflater: LayoutInflater, container: ViewGroup?) {
        adapterManager = LinearLayoutManager(requireContext())
        adapter = SearchFragmentAdapter(mContext)
        binding.searchRecyclerView.adapter = adapter
        binding.searchRecyclerView.layoutManager = adapterManager
        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.searchRecyclerView.canScrollVertically(1)) {
                    Toast.makeText(requireContext(), "마지막", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    fun setupListeners() {
        binding.searchBtn.setOnClickListener {
            query = binding.searchInput.text.toString()
            adapter.newsList.clear()
            viewModel.getNewsList(query, 1)

            //키보드 내리는 기능
//            val imm =
//                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
        }
    }
}