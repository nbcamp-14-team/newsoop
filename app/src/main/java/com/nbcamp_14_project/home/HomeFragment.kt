package com.nbcamp_14_project.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.databinding.FragmentMainBinding
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.detail.DetailViewModelFactory
import com.nbcamp_14_project.mainpage.MainActivity
import com.nbcamp_14_project.ui.login.LoginViewModel
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.util.Timer
import java.util.TimerTask

class HomeFragment() : Fragment() {
    companion object {
        private var firstHomeCategory: String? = null
        private var secondHomeCategory: String? = null
        private var thirdHomeCategory: String? = null
    }


    private lateinit var queryInbox:String // 카테고리 변수
    var isLoading = false // 인피니티 스크롤이 로딩되었는지 체크하는 변수
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel: DetailViewModel by activityViewModels{DetailViewModelFactory()} // detailFragment SharedViewModel
    private val fireStore = FirebaseFirestore.getInstance() // FireStore instance
    private var isUserHaveCategory: Boolean = false//User가 카테고리를 가지고 있는지 체크하는 변수

    // HomeFragment Fragment
    private val headLineAdapter by lazy {
        HomeHeadLineAdapter(//ViewPager2에서 클릭이 일어났을 때, Detail에 데이터값을 보냄
            onClick = { item ->
                val detailInfo =
                    item.toDetailInfo()//클릭된 아이템의 data class를 toDetailInfo data class로 수정
                detailViewModel.setDetailInfo(detailInfo)//뷰모델로 전송
                Log.d("hyunsik", "detalInfo = $detailInfo")
                val mainActivity = (activity as MainActivity)
                mainActivity.runDetailFragment()//DetailFragment 실행
            }
        )
    }
    private val newsAdapter by lazy {
        HomeNewsAdapter(//recyclerView에서 클릭이 일어났을 때, Detail에 데이터값을 보냄
            onClick = { item ->
                val detailInfo =
                    item.toDetailInfo()//클릭된 아이템의 data class를 toDetailInfo data class로 수정
                detailViewModel.setDetailInfo(detailInfo)//뷰모델로 전송
                Log.d("hyunsik", "detalInfo = $detailInfo")
                val mainActivity = (activity as MainActivity)
                mainActivity.runDetailFragment()//DetailFragment 실행
            }
        )
    }
    private val viewPagerViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this, HomeModelFactory()
        )[HomeViewModel::class.java]
    }
    private val homeViewPagerViewModel: HomeViewPagerViewModel by activityViewModels {
        HomeViewPagerViewModelFactory()
    }
    private var startingNum: Int = 7// 인피니티스크롤 시작지점 지정 변수


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        queryInbox = arguments?.getString("query").toString() // 뷰를 생성할 때 번들로 검색어를 받음
        Log.d("queryInbox","$queryInbox")
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()//화면 설정 함수
        initViewModel()//뷰모델 설정 함수
        if (queryInbox != "추천") { //탭이 추천이 아닐 때 실행
            Log.d("isworkAtViewCreated","$queryInbox")
            viewPagerViewModel.headLineNews(queryInbox!!)
            viewPagerViewModel.detailNews(queryInbox!!)
        }
    }

    fun initView() = with(binding) {
        category.text = queryInbox + " 관련 뉴스" //카테고리 텍스트박스
        val slideImageHandler: Handler = Handler()
        val slideImageRunnable = Runnable {// 메인 ViewPager 자동 스와이프 관련 runnable 함수

            rvMainNews.currentItem = rvMainNews.currentItem + 1
            if (rvMainNews.currentItem == headLineAdapter.itemCount) {
                rvMainNews.currentItem = 0
            }
        }// viewpager의 Item Position을 1 추가하여 페이지 이동하게 구현

        /**
         * ViewPager 2 세팅
         */
        rvMainNews.adapter = headLineAdapter//어뎁터 연결
        rvMainNews.orientation = ViewPager2.ORIENTATION_HORIZONTAL//가로로 설정
        rvMainNews.apply {
            offscreenPageLimit = 1
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    slideImageHandler.removeCallbacks(slideImageRunnable)//handler에 뷰페이저 이동 꽇음
                    slideImageHandler.postDelayed(slideImageRunnable, 3000)//handler에 딜레이 추가
                }
            })
        }
        rvMainNewsIndicator.attachTo(rvMainNews)
        /**
         * 하단 리사이클러뷰 세팅
         */
        val decoration = DividerItemDecoration(requireContext(), VERTICAL)
        rvNews.addItemDecoration(decoration)

        rvNews.adapter = newsAdapter//어뎁터 연결
        rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //인피니티 스크롤 구현
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisiblePosition =
                    (rvNews.layoutManager as LinearLayoutManager)!!.findLastCompletelyVisibleItemPosition()//마지막으로 보인 Item에 포지션변수
                val itemCount = newsAdapter.itemCount - 1//Adapter아이템 개수
                Log.d("LoadingView", "${viewPagerViewModel.isLoading}")
                if (viewPagerViewModel.isLoading) {
                    isLoading = false
                }
                Log.d("VisiblePosition", "$lastVisiblePosition + $itemCount + $isLoading")
                if (!isLoading) {
                    /**
                     * 스크롤이 더이상 안 될 때 인피니티 스크롤 실행
                     */
                    if (!rvNews.canScrollHorizontally(1)
                        && (rvNews.layoutManager as LinearLayoutManager)!!.findLastCompletelyVisibleItemPosition() == itemCount
                        && itemCount != -1) {
                        isLoading = true
                        infinityAddNews(queryInbox)
                    }
                }
            }
        })

    }


    private fun initViewModel() {
        with(viewPagerViewModel) {
            list.observe(viewLifecycleOwner) {
                headLineAdapter.submitList(it.toList())

            }
            newsList.observe(viewLifecycleOwner) {
                newsAdapter.submitList(it.toList())
            }
        }

        with(homeViewPagerViewModel) {
            resumed.observe(viewLifecycleOwner) {
                Log.d("queryInbox", "$queryInbox")
                try {
                    recommendTab(queryInbox)
                } catch (e: HttpException) {
                    Log.d("HttpException", "atViewModel")
                }

            }
        }
    }

    private fun infinityAddNews(query: String?) {
        if (queryInbox == "추천") {
            if (firstHomeCategory.isNullOrBlank()) {
                viewPagerViewModel.detailNewsInfinity("생활", startingNum)
                startingNum += 10
                viewPagerViewModel.isLoading = false
            } else {
                viewPagerViewModel.detailNewsInfinityToRecommend(
                    firstHomeCategory, secondHomeCategory,
                    thirdHomeCategory, startingNum
                )
                viewPagerViewModel.isLoading = false
                startingNum += 10
            }

        } else {
            viewPagerViewModel.detailNewsInfinity(query!!, startingNum)

            viewPagerViewModel.isLoading = false
            startingNum += 10
        }

    }

    private fun recommendTab(query: String?) {

        if (query == "추천") { //추천 프래그먼트일 시 ,
            Log.d("isWork", "$isUserHaveCategory")
            /**
             * 파이어베이스에서 카테고리 가져오기
             */
            val collectionRef = fireStore.collection("User")
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: return)
            collectionRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        var firstCategory = document.getString("category")
                        var secondCategory = document.getString("secondCategory")
                        var thirdCategory = document.getString("thirdCategory")
                        if (secondHomeCategory.isNullOrBlank()) secondHomeCategory = null
                        if (thirdHomeCategory.isNullOrBlank()) thirdHomeCategory = null
                        if (firstHomeCategory.isNullOrBlank()) firstHomeCategory = null
                        if (firstCategory.isNullOrBlank()) firstCategory = null
                        if (secondCategory.isNullOrBlank()) secondCategory = null
                        if (thirdCategory.isNullOrBlank()) thirdCategory = null

                        if (firstHomeCategory == firstCategory
                            && secondHomeCategory == secondCategory
                            && thirdHomeCategory == thirdCategory
                            && !isUserHaveCategory
                        ) {
                            Log.d("iswork","7")
                            firstHomeCategory = null
                            viewPagerViewModel.clearAllItems()
                            viewPagerViewModel.headLineNews("정치")//메인 ViewPager에 헤드라인 뉴스 출력
                            viewPagerViewModel.detailNews("정치", 1)//하단 리사이클러뷰에 뉴스 출력
                            isUserHaveCategory = true
                        }
                        if (firstHomeCategory == firstCategory
                            && secondHomeCategory == secondCategory
                            && thirdHomeCategory == thirdCategory
                        ) {
                            return@addOnCompleteListener
                        } else {

                            viewPagerViewModel.clearAllItems()
                            Log.d("testCategory", "${document.getString("category")}")
                            firstHomeCategory = document.getString("category")
                            secondHomeCategory = document.getString("secondCategory")
                            thirdHomeCategory = document.getString("thirdCategory")
                            viewPagerViewModel.category = firstHomeCategory
                            if (firstHomeCategory.isNullOrBlank()) firstHomeCategory = null

                            if (secondHomeCategory.isNullOrBlank()) {
                                Log.d("isWork?", "3")
                                isUserHaveCategory = true
                                secondHomeCategory = null
                                viewPagerViewModel.headLineNews(
                                    firstHomeCategory ?: "생활",
                                    5
                                )
                                viewPagerViewModel.detailNews(
                                    firstHomeCategory ?: "생활",
                                    1
                                )
                            } else if (thirdHomeCategory.isNullOrBlank()) {
                                Log.d("isWork?", "2")
                                isUserHaveCategory = true
                                thirdHomeCategory = null
                                viewPagerViewModel.headLineNews(
                                    firstHomeCategory ?: "생활",
                                    3
                                )
                                viewPagerViewModel.headLineNews(
                                    secondHomeCategory ?: "생활",
                                    2
                                )
                                viewPagerViewModel.twoCategoryDetailNews(
                                    firstCategory,
                                    secondCategory,
                                    1
                                )
                            } else if (!firstHomeCategory.isNullOrBlank()) {
                                isUserHaveCategory = true
                                Log.d("isWork?", "1")
                                viewPagerViewModel.headLineNews(
                                    firstHomeCategory ?: "생활", 2
                                )
                                viewPagerViewModel.headLineNews(
                                    secondHomeCategory ?: "생활",
                                    2
                                )
                                viewPagerViewModel.headLineNews(
                                    thirdHomeCategory ?: "생활",
                                    1
                                )
                                viewPagerViewModel.threeCategoryDetailNews(
                                    firstCategory,
                                    secondCategory,
                                    thirdCategory,
                                    1
                                )

                            }
                        }
                    }
                } else {
                    Log.d("iswork", "fail")
                }
            }
        }
    }

}




