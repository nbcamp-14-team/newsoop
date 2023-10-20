package com.nbcamp_14_project.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentMainBinding
import com.nbcamp_14_project.detail.DetailFragment
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.favorite.FavoriteViewModel
import com.nbcamp_14_project.mainpage.MainActivity
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.search.SearchFragment

@Suppress("DEPRECATION")
class Home : Fragment() {
    companion object {
        fun newInstance() = Home()
    }
    private val changeHandler = Handler()
    private var _binding: FragmentMainBinding? = null
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val binding get() = _binding!!
    // Home Fragment
    private val headLineAdapter by lazy {
        HomeHeadLineAdapter(//ViewPager2에서 클릭이 일어났을 때, Detail에 데이터값을 보냄(구현필요)
            onClick = {item ->
                val detailInfo = item.toDetailInfo()
                detailViewModel.setDetailInfo(detailInfo)
                val mainActivity = (activity as MainActivity)
                mainActivity.test()
            }
        )
    }
    private val newsAdapter by lazy {
        HomeNewsAdapter(//recyclerView에서 클릭이 일어났을 때, Detail에 데이터값을 보냄(구현필요)
            onClick = { item ->
                val detailInfo = item.toDetailInfo()
                detailViewModel.setDetailInfo(detailInfo)
                val mainActivity = (activity as MainActivity)
                mainActivity.test()
            }
        )
    }
    private val viewPagerViewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(
            this, MainFragmentModelFactory()
        )[MainFragmentViewModel::class.java]
    }
    private var startingNum:Int = 6// 인피니티스크롤 시작지점 지정 변수
    private var query = "정치"//검색어 변수
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        viewPagerViewModel.headLineNews(query)
        viewPagerViewModel.detailNews(query)


    }
    fun changeCategory(query:String?) = with(binding){
        viewPagerViewModel.clearAllItems()
        viewPagerViewModel.headLineNews(query!!)
        viewPagerViewModel.detailNews(query!!)
        category.text = query
    }

    fun initView() = with(binding) {
        val slideImageHandler: Handler = Handler()
        val slideImageRunnable = Runnable {
            if (rvMainNews.currentItem - 1 == headLineAdapter.itemCount) {
                rvMainNews.currentItem = 0
            }
            rvMainNews.currentItem = rvMainNews.currentItem + 1

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
        rvMainNewsIndicator.setViewPager2(rvMainNews)
        /**
         * 하단 리사이클러뷰 세팅
         */

        rvNews.adapter = newsAdapter//어뎁터 연결
        rvNews.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisiblePosition = (rvNews.layoutManager as LinearLayoutManager)!!.findLastCompletelyVisibleItemPosition()
                val itemCount = newsAdapter.itemCount - 1
                Log.d("VisiblePosition","$lastVisiblePosition + $itemCount")

                if(!rvNews.canScrollHorizontally(1) && lastVisiblePosition == itemCount){
                    infinityAddNews(query)
                }
            }
        })

        /**
         * 버튼 세팅
         */
        btnCulture.setOnClickListener{
            changeCategory("문화")
            query = "문화"
            startingNum = 6
        }
        btnEconomy.setOnClickListener {
            changeCategory("경제")
            query = "경제"
            startingNum = 6
        }
        btnIt.setOnClickListener {
            changeCategory("it")
            query = "it"
            startingNum = 6
        }
        btnLife.setOnClickListener {
            changeCategory("생활")
            query = "생활"
            startingNum = 6
        }
        btnPolitic.setOnClickListener {
            changeCategory("정치")
            query = "정치"
            startingNum = 6
        }
        btnScience.setOnClickListener {
            changeCategory("과학")
            query = "과학"
            startingNum = 6
        }
        btnSociety.setOnClickListener {
            changeCategory("사회")
            query = "사회"
            startingNum = 6
        }
        btnWorld.setOnClickListener {
            changeCategory("세계")
            query = "세계"
            startingNum = 6
        }
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
    private fun infinityAddNews(query:String?){
        viewPagerViewModel.detailNews(query!!,startingNum)
        startingNum += 6
    }
}

