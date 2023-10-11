package com.nbcamp_14_project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.api.NewsDTO
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment: Fragment() {
    companion object{
        fun newInstance() = MainFragment()
    }
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!

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
        Utils.getNews("항저우 아시안게임")
        initView()
    }
    fun initView() = with(binding){
        ivPolitics.load("http://app.yonhapnews.co.kr/YNA/Basic/SNS/r.aspx?c=AKR20160926019000008&did=1195m")
    }

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