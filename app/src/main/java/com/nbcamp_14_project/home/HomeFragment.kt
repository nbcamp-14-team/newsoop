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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    private val fireStore = FirebaseFirestore.getInstance()

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
        initView()//화면 설정 함수
        initViewModel()//뷰모델 설정 함수

        if (queryInbox == "추천") { //추천 프래그먼트일 시 ,
            var firstCategory: String? = null
            var secondCategory: String? = null
            var thirdCategory: String? = null
            /**
             * 파이어베이스에서 카테고리 가져오기
             */
            val collectionRef = fireStore.collection("User")
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: return)
            collectionRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        Log.d("testCategory","${document.getString("category")}")
                        firstCategory = document.getString("category")
                        secondCategory = document.getString("secondCategory")
                        thirdCategory = document.getString("thirdCategory")
                        Log.d("firstCategory","$firstCategory")
                        Log.d("secondCategory","$secondCategory")
                        Log.d("thirdCategory","$thirdCategory")


                        if(secondCategory == null){
                            viewPagerViewModel.headLineNews(firstCategory?:"생활", 5)
                            viewPagerViewModel.detailNews(firstCategory?:"생활",1,5)
                        }else if(thirdCategory == ""){
                            Log.d("isWork?","")
                            viewPagerViewModel.headLineNews(firstCategory?:"생활", 3)
                            viewPagerViewModel.detailRecommendNews(firstCategory?:"생활",1,3)
                            viewPagerViewModel.headLineNews(secondCategory?:"생활",2)
                            viewPagerViewModel.detailNews(secondCategory?:"생활",2)
                        }else{
                            viewPagerViewModel.headLineNews(firstCategory?:"생활",2)
                            viewPagerViewModel.detailRecommendNews(firstCategory?:"생활",1,2)
                            viewPagerViewModel.headLineNews(secondCategory?:"생활",2)
                            viewPagerViewModel.detailRecommendNews(secondCategory?:"생활",1,2)
                            viewPagerViewModel.headLineNews(thirdCategory?:"생활",1)
                            viewPagerViewModel.detailNews(secondCategory?:"생활",2)
                        }

                    } else {
                        Log.d("data", "no data")
                    }
                }
                /**
                 * 카테고리 널세이프 적용
                 */
            }

        } else {
            Log.d("query", "$queryInbox")

            viewPagerViewModel.headLineNews(queryInbox)//메인 ViewPager에 헤드라인 뉴스 출력
            viewPagerViewModel.detailNews(queryInbox, 1)//하단 리사이클러뷰에 뉴스 출력
        }


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
        if(queryInbox == "추천"){

        }else{
            viewPagerViewModel.detailNewsInfinity(query!!, startingNum)
            startingNum += 6
            viewPagerViewModel.isLoading = false
        }

    }
}

