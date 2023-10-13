package com.nbcamp_14_project.Main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.api.Items
import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.api.NewsDTO
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.databinding.FragmentMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.job
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment: Fragment() {
    companion object{
        fun newInstance() = MainFragment()
    }
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val listAdapter by lazy{
        MainFragmentAdapter()
    }
    private val viewModel: MainFragmentViewModel by lazy{
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
//        Utils.getNews("항저우 아시안게임")
        initView()
        initViewModel()
        viewModel.search("오늘의 뉴스")
//        getThumbnail("https://www.yna.co.kr/view/AKR20231012007700079?section=international/all&site=major_news01")
//        test()
    }
    fun initView() = with(binding){
        rvNews.adapter = listAdapter
    }
    fun initViewModel() {
        with(viewModel){
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