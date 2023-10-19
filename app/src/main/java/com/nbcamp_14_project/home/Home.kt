package com.nbcamp_14_project.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentMainBinding
import com.nbcamp_14_project.detail.DetailActivity
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.search.SearchFragment

@Suppress("DEPRECATION")
class Home : Fragment() {
    companion object {
        fun newInstance() = Home()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val headLineAdapter by lazy {
        HomeHeadLineAdapter(//ViewPager2에서 클릭이 일어났을 때, Detail에 데이터값을 보냄(구현필요)
            onClick = {item ->
                val detailInfo = item.toDetailInfo()
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("detail_model_key", detailInfo)

                startActivity(intent)

            }
        )
    }
    private val newsAdapter by lazy {
        HomeNewsAdapter(//recyclerView에서 클릭이 일어났을 때, Detail에 데이터값을 보냄(구현필요)
            onClick = {item ->
                val detailInfo = item.toDetailInfo()
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("detail_model_key", detailInfo)

                startActivity(intent)
                activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

            }
        )
    }
    private val viewPagerViewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(
            this, MainFragmentModelFactory()
        )[MainFragmentViewModel::class.java]
    }
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
        viewPagerViewModel.headLineNews("헤드라인 뉴스")
        viewPagerViewModel.detailNews("정치 관련 뉴스")


    }
    fun changeCategory(query:String?) = with(binding){
        viewPagerViewModel.clearAllItems()
        viewPagerViewModel.headLineNews(query + "메인 뉴스")
        viewPagerViewModel.detailNews(query+"관련 뉴스")
        category.text = query
    }

    fun initView() = with(binding) {
        val slideImageHandler: Handler = Handler()
        val slideImageRunnable = Runnable {
            if(rvMainNews.currentItem -1  == headLineAdapter.itemCount){
                rvMainNews. currentItem = 0
            }
            rvMainNews.currentItem = rvMainNews.currentItem + 1

        }// viewpager의 Item Position을 1 추가하여 페이지 이동하게 구현

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

        rvNews.adapter = newsAdapter//어뎁터 연결
        //버튼 클릭리스너
        btnCulture.setOnClickListener{
            changeCategory("문화")
        }
        btnEconomy.setOnClickListener {
            changeCategory("경제")
        }
        btnIt.setOnClickListener {
            changeCategory("it")
        }
        btnLife.setOnClickListener {
            changeCategory("생활")
        }
        btnPolitic.setOnClickListener {
            changeCategory("정치")
        }
        btnScience.setOnClickListener {
            changeCategory("과학")
        }
        btnSociety.setOnClickListener {
            changeCategory("사회")
        }
        btnWorld.setOnClickListener {
            changeCategory("세계")
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
}

