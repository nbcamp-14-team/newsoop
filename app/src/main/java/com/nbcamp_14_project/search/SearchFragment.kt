package com.nbcamp_14_project.search

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.nbcamp_14_project.R.layout.item_loading
import com.nbcamp_14_project.databinding.FragmentSearchBinding
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.home.toDetailInfo
import com.nbcamp_14_project.mainpage.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val dialog by lazy { LoadingDialog(requireContext()) }
    private var _binding: FragmentSearchBinding? = null
    private val detailViewModel: DetailViewModel by activityViewModels()

    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this, SearchViewModelFactory()
        )[SearchViewModel::class.java]
    }
    private val adapter by lazy {
        SearchListAdapter(
            onClick = { item ->
                val detailInfo = item.toDetailInfo()
                detailViewModel.setDetailInfo(detailInfo)
                val mainActivity = (activity as MainActivity)
                mainActivity.test()
            }
        )
    }
    private val tagAdapter by lazy { SearchTagAdapter() }

    private var query = ""
    private var countStart = 6

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initView() = with(binding) {
        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchRecyclerView.adapter = adapter
        searchTagFrame.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, true)
        searchTagFrame.adapter = tagAdapter
        searchInput.setOnEditorActionListener { _, i, _ ->
            var handled = false
            if (i == EditorInfo.IME_ACTION_DONE) {
                searchBtn.performClick()
                handled = true
            }
            handled
        }

        searchBtn.setOnClickListener {
            query = binding.searchInput.text.toString()
            viewModel.clearAllItems()
            viewModel.setRecentSearchItem(query)
            viewModel.getSearchNews(query, 5, 1)
            //키보드 내리는 기능
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchInput.windowToken, 0)
            //edit 비우기
            binding.searchInput.text.clear()
            CoroutineScope(Dispatchers.Main).launch {
                dialog.show()
                delay(3000)
                dialog.dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisiblePosition =
                    (binding.searchRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemCount = adapter.itemCount - 1
                Log.d("VisiblePosition", "$lastVisiblePosition + $itemCount")

                if (!binding.searchRecyclerView.canScrollHorizontally(1) && lastVisiblePosition == itemCount) {
                    viewModel.getSearchNews(query, 5, countStart)
                    countStart += 6
                    CoroutineScope(Dispatchers.Main).launch {
                        dialog.show()
                        delay(3000)
                        dialog.dismiss()
                    }
                }
            }
        })

        tagAdapter.setItemClickListener(object : SearchTagAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

                // 최근 검색어 가져오기
                val list = viewModel.recentSearchList.value!!
                binding.searchInput.setText(list[position])
                binding.searchBtn.performClick()
                // 똑같은 검색어 지우기
                viewModel.removeRecentSearchItem(position)
            }
        })
    }

    private fun initViewModel() {
        with(viewModel) {
            searchResultList.observe(viewLifecycleOwner) {
                adapter.submitList(it.toMutableList())
            }
            recentSearchList.observe(viewLifecycleOwner) {
                tagAdapter.submitList(it.toMutableList())
            }
        }
    }

    class LoadingDialog(context: Context) : Dialog(context) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(item_loading)
            // 취소 불가능
            setCancelable(false)
            // 배경 투명하게 바꿔줌
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}