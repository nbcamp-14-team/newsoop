package com.nbcamp_14_project.search

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R.layout.item_loading
import com.nbcamp_14_project.databinding.FragmentSearchBinding
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.detail.DetailRepository
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.detail.DetailViewModelFactory
import com.nbcamp_14_project.favorite.FavoriteRepository
import com.nbcamp_14_project.favorite.FavoriteViewModel
import com.nbcamp_14_project.home.HomeViewPagerViewModel
import com.nbcamp_14_project.home.HomeViewPagerViewModelFactory
import com.nbcamp_14_project.home.toDetailInfo
import com.nbcamp_14_project.mainpage.MainActivity
import com.nbcamp_14_project.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class SearchFragment : Fragment() {
    companion object {
        fun newInstance() = SearchFragment()
    }

    private val dialog by lazy { LoadingDialog(requireContext()) }
    private var _binding: FragmentSearchBinding? = null
    private val detailViewModel: DetailViewModel by activityViewModels{DetailViewModelFactory()}
//    private val favoriteViewModel: FavoriteViewModel by activityViewModels{FavoriteViewModel.FavoriteViewModelFactory()}
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            requireActivity(), SearchViewModelFactory()
        )[SearchViewModel::class.java]
    }
    private val homeViewPagerViewModel: HomeViewPagerViewModel by activityViewModels{
        HomeViewPagerViewModelFactory()
    }
    private val checkLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val checkLogin = result.data?.getStringExtra(LoginActivity.CHECK_LOGIN)
                if (checkLogin == "Login") {
                    homeViewPagerViewModel.addListAtFirst("추천", "추천")
                }
            }
        }
    var user = FirebaseAuth.getInstance().currentUser

    private val adapter by lazy {
        SearchListAdapter(
            onClick = { item ->
                val detailInfo = item.toDetailInfo()
                Log.d("hyunsik", "detailInfo=$detailInfo")
                detailViewModel.setDetailInfo(detailInfo)
                val mainActivity = (activity as MainActivity)
                mainActivity.runDetailFragment()
            },
            onSwitch = { item ->
                val detailInfo = item.toDetailInfo()
                val test = user
                Log.d("searchuserFragment", "$test")
                if (test != null) {
                    // 사용자가 로그인한 경우
                    if (detailInfo != null) {
                        val isFavorite = detailInfo.isLike
                        if (!isFavorite!!) {
                            removeFavoriteFromFireStore(detailInfo)  // Firestore에서도 제거
                        } else {
                            addFavoriteToFireStore(detailInfo)  // Firestore에도 추가
                        }
                    }
                } else {
                    // 사용자가 로그인하지 않은 경우
                    showSnackbar("로그인을 해주세요.")
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    checkLoginLauncher.launch(intent)
                }
            }
        )
    }
    private val tagAdapter by lazy { SearchTagAdapter() }
    private var query = ""
    private var countStart = 11

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
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
                    viewModel.getSearchNews(query, 10, countStart)
                    countStart += 10
                    //검색 로딩 딜레이 주기 : 3초
                    CoroutineScope(Dispatchers.Main).launch {
                        dialog.show()
                        delay(3000)
                        dialog.dismiss()
                    }
                }
            }
        })

        tagAdapter.setItemClickListener(object : SearchTagAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, searchWord: String) {
                // 최근 검색어 가져오기
                binding.searchInput.setText(searchWord)
                Log.d("search", "$position : $searchWord")
                binding.searchBtn.performClick()
            }

            override fun onCancelClick(v: View, position: Int, searchWord: String) {
                viewModel.removeRecentSearchItem(searchWord)
                Log.d("search", "$position : $searchWord")
            }

        })
    }

    override fun onResume() {
        super.onResume()
        user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            viewModel.clearRecentSearch()
            getRecentSearchListFirebase(userUID = user!!.uid)
        }
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
            //최근 검색어에서 같은 단어가 있으면 지우고 새로 넣기
            viewModel.removeRecentSearchItem(query)
            viewModel.clearAllItems()
            viewModel.addRecentSearchItem(query)
            viewModel.getSearchNews(query, 10, 1)

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

    private fun initViewModel() {
        with(viewModel) {
            searchResultList.observe(viewLifecycleOwner) {
                // 리스트가 비어 있으면 로고를 표시, 반대면 비표시
                if (it.isNotEmpty()) {
                    binding.searchNot.visibility = View.GONE
                } else {
                    binding.searchNot.visibility = View.VISIBLE
                }
                adapter.submitList(it.toMutableList())
            }
            recentSearchList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    binding.recentSearchNot.visibility = View.GONE
                } else {
                    binding.recentSearchNot.visibility = View.VISIBLE
                }
                Log.d("recent", "${it.size}")
                tagAdapter.submitList(it.toMutableList())
            }
        }
    }

    private fun getRecentSearchListFirebase(userUID: String) {
        val db = FirebaseFirestore.getInstance()
        val recentSearchCollection =
            db.collection("User").document(userUID).collection("recentSearch")
        recentSearchCollection.orderBy("inputTime").get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val searchWord = document.getString("searchWord")
                    if (searchWord != null) {
                        viewModel.setRecentSearchItem(searchWord)
                    }
                }
                Log.d("recentSearchFirebase", "is success")
            }
            .addOnFailureListener { e ->
                Log.d("recentSearchFirebase", "is fail")
            }
    }

    private fun addFavoriteToFireStore(detailInfo: DetailInfo) {
        // 1. 사용자가 로그인한 후 사용자 UID 가져오기
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid
        if (userUID != null) {
            // 2. Firestore에서 해당 사용자의 favorite 컬렉션 참조
            val db = FirebaseFirestore.getInstance()
            val favoriteCollection = db.collection("User").document(userUID).collection("favorites")
            val favoriteData = hashMapOf(
                "title" to detailInfo.title,
                "thumbnail" to detailInfo.thumbnail,
                "description" to detailInfo.description,
                "author" to detailInfo.author,
                "originalLink" to detailInfo.originalLink,
                "pubDate" to detailInfo.pubDate,
                "created" to Date(),
                "isLike" to true
            )

            favoriteCollection.document(detailInfo.title.toString()).set(favoriteData)
        } else {
//            Toast.makeText(requireContext(), "로그인을 해주세요".toString() )show()
        }

    }

    private fun removeFavoriteFromFireStore(detailInfo: DetailInfo) {
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid ?: return

        val db = FirebaseFirestore.getInstance()
        val favoriteCollection = db.collection("User").document(userUID).collection("favorites")
        val query = favoriteCollection.whereEqualTo("title", detailInfo.title)

        query.get().addOnSuccessListener { documents ->
            for (document in documents) {
                document.reference.delete()
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }


    // 리스트 로딩을 위한 Dialog
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