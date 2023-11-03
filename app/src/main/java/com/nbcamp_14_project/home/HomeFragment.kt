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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.navercorp.nid.NaverIdLoginSDK.applicationContext
import com.nbcamp_14_project.databinding.FragmentMainBinding
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.mainpage.MainActivity
import kotlinx.coroutines.delay

class HomeFragment(query: String) : Fragment() {


    companion object {
        fun newInstance(query: String) = HomeFragment(query)
    }

    private var queryInbox = query //검색어 변수
    var isLoading = false

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel: DetailViewModel by activityViewModels()

    // HomeFragment Fragment
    private val headLineAdapter by lazy {
        HomeHeadLineAdapter(//ViewPager2에서 클릭이 일어났을 때, Detail에 데이터값을 보냄
            onClick = { item ->
                val detailInfo =
                    item.toDetailInfo()//클릭된 아이템의 data class를 toDetailInfo data class로 수정
                detailViewModel.setDetailInfo(detailInfo)//뷰모델로 전송
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
    private var startingNum: Int = 6// 인피니티스크롤 시작지점 지정 변수


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("query", "$queryInbox")
        initView()//화면 설정 함수
        initViewModel()//뷰모델 설정 함수
        viewPagerViewModel.headLineNews(queryInbox)//메인 ViewPager에 헤드라인 뉴스 출력
        viewPagerViewModel.detailNews(queryInbox, 0)//하단 리사이클러뷰에 뉴스 출력

    }

    fun initView() = with(binding) {
        category.text = queryInbox + " 관련 뉴스"//텍스트박스
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

                    if (!rvNews.canScrollHorizontally(1) && (rvNews.layoutManager as LinearLayoutManager)!!.findLastCompletelyVisibleItemPosition() == itemCount) {
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
                headLineAdapter.submitList(it)

            }
            newsList.observe(viewLifecycleOwner) {
                newsAdapter.submitList(it)
            }
        }
    }

    private fun infinityAddNews(query: String?) {
        viewPagerViewModel.detailNewsInfinity(query!!, startingNum)
        startingNum += 6
        viewPagerViewModel.isLoading = false
    }
}

