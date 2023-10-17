package com.nbcamp_14_project.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.nbcamp_14_project.databinding.FragmentMainBinding
import java.util.Timer
import java.util.TimerTask

@Suppress("DEPRECATION")
class Home: Fragment() {
    companion object{
        fun newInstance() = Home()
    }
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val listAdapter by lazy{
        HomeAdapter()
    }
    private val viewPagerViewModel: MainFragmentViewModel by lazy{
        ViewModelProvider(
            this,
            MainFragmentModelFactory()
        )[MainFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerViewModel.search("헤드라인 뉴스")
        initView()
        initViewPagerViewModel()

//        getThumbnail("https://www.yna.co.kr/view/AKR20231012007700079?section=international/all&site=major_news01")
//        test()
    }
    fun initView() = with(binding){
//        rvNews.adapter = listAdapter
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val slideImageHandler: Handler = Handler()
        val slideImageRunnable = Runnable{rvMainNews.currentItem = rvMainNews.currentItem + 1}// viewpager의 Item Position을 1 추가하여 페이지 이동하게 구현

        rvMainNews.adapter=listAdapter//어뎁터 연결
        rvMainNews.orientation=ViewPager2.ORIENTATION_HORIZONTAL//가로로 설정
        rvMainNews.apply {
            offscreenPageLimit = 1
            registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    slideImageHandler.removeCallbacks(slideImageRunnable)//handler에 뷰페이저 이동 꽇음
                    slideImageHandler.postDelayed(slideImageRunnable, 3000)//handler에 딜레이 추가
                }
            })
        }
        rvMainNewsIndicator.setViewPager2(rvMainNews)
        //PagerSnapHelper 추가
//        rvMainNews.apply{
//
//        }

//        val snapHelper = PagerSnapHelper()
//        snapHelper
    //        rvMainNews.layoutManager = layoutManager
//        snapHelper.attachToRecyclerView(rvMainNews)
//        val timer = Timer()
//        val scrollTask = object: TimerTask() {
//            override fun run(){
//                run{
//                    val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
//                    if(currentPosition<listAdapter.itemCount -1){
//                        rvMainNews.smoothScrollToPosition(currentPosition + 1)
//                    }else{
//                        rvMainNews.smoothScrollToPosition(0)
//                    }
//                }
//            }
//        }
//        timer.schedule(scrollTask,3000,3000)


    }
    fun initViewPagerViewModel() {
        with(viewPagerViewModel){
            list.observe(viewLifecycleOwner){
                listAdapter.submitList(it)
            }
        }
    }
//    suspend fun test(){
//        var data:NewsDTO? = null
//        var newsList: List<Items>? = null
//        RetrofitInstance.api.getNews(NewsCollector.ID, NewsCollector.SECRET,"오늘의 뉴스").enqueue(object:
//            Callback<NewsDTO> {
//
//            override fun onResponse(call: Call<NewsDTO>, response: Response<NewsDTO>) {
//                if(response.isSuccessful){
//                    Log.d("APiSuccess","Success")
//                    data = response.body()
//                    newsList = data?.items ?: return
//
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewsDTO>, t: Throwable) {
//                Log.d("APIfail","Fail")
//            }
//        })
//        for(i in newsList!!.indices){
//            val link = newsList!![i].link
//            var thumbnail = Utils.getThumbnail(link.toString())
//            Log.d("thumbnail","$thumbnail")
//
//            val description = newsList!![i].description
//        }
//    }
//
//    fun getThumbnail(url:String){
//        Log.d("success1","success1")
//        Thread{
////            val url = "https://www.yna.co.kr/view/AKR20231012007700079?section=international/all&site=major_news01"
//            val docs = Jsoup.connect(url).get()
//            Log.d("docs","$docs")
//            val test = docs.select("meta[property=og:image]").attr("content")
//            Log.d("select","$test")
//        }.start()
//    }

//    fun getNews(){
//        var testData= "항저우 아시안게임"
//        RetrofitInstance.api.getNews(NewsCollector.ID,NewsCollector.SECRET,testData)?.enqueue(object:
//            Callback<NewsDTO>{
//            override fun onResponse(call: Call<NewsDTO>, response: Response<NewsDTO>) {
//                if(response.isSuccessful){
//                    Log.d("APiSuccess","Success")
//                    val data = response.body()
//                    Log.d("test","$data")
//                    val newsList = data?.items
//                    var testData = newsList!![0].description
//                    Log.d("newsList","$newsList")
//                }
//            }
//
//            override fun onFailure(call: Call<NewsDTO>, t: Throwable) {
//                Log.d("APIfail","Fail")
//            }
//        })
//    }

}