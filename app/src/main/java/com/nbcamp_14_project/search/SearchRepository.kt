package com.nbcamp_14_project.search

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.domain.SearchEntity
import com.nbcamp_14_project.domain.toSearchEntity
import com.nbcamp_14_project.home.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger


interface SearchRepository {
    suspend fun getNews(
        query: String,
        display: Int? = null,
        start: Int? = null
    ): SearchEntity

    fun getList(): List<HomeModel>
    fun addNewsItem(item: HomeModel?): List<HomeModel>
    fun clearList(): List<HomeModel>
    fun getRecentSearchList(): List<String>
    fun addRecentSearchList(searchWord: String): List<String>
    fun removeRecentSearchItem(searchWord: String): List<String>
}

class SearchRepositoryImpl(
    private val idGenerate: AtomicInteger,
    private val remoteDatasource: NewsCollector
) : SearchRepository {
    private val searchList = mutableListOf<HomeModel>()
    private val recentSearchList = mutableListOf<String>()
    val user = FirebaseAuth.getInstance().currentUser
    val userUID = user?.uid


    //시간이 오래 걸림 ->
    override suspend fun getNews(query: String, display: Int?, start: Int?): SearchEntity =
        withContext(Dispatchers.IO) {
            remoteDatasource.getNews(
                query,
                display,
                start
            ).toSearchEntity()
        }


    override fun getList(): List<HomeModel> {
        return searchList
    }

    override fun addNewsItem(item: HomeModel?): List<HomeModel> {
        if (item == null) {
            return searchList
        }
        searchList.add(item.copy(id = idGenerate.getAndIncrement()))
        return searchList
    }

    override fun clearList(): List<HomeModel> {
        searchList.clear()
        return ArrayList<HomeModel>(searchList)
    }

    override fun getRecentSearchList(): List<String> {
        return recentSearchList
    }

    override fun addRecentSearchList(searchWord: String): List<String> {
        if (searchWord == "") {
            return recentSearchList
        }


        //userUID가 존재하면 firebase에 저장
        if (userUID != null) {
            val nowTime = LocalDateTime.now()
            val inputTime = nowTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"))
            val db = FirebaseFirestore.getInstance()
            val recentSearchCollection =
                db.collection("User").document(userUID).collection("recentSearch")
            val recentSearchData = hashMapOf(
                "searchWord" to searchWord,
                "inputTime" to inputTime,
            )
            recentSearchCollection.add(recentSearchData).addOnSuccessListener {
                Log.d("searchFirebase", "is success : $userUID")
            }.addOnFailureListener {
                Log.d("searchFirebase", "is fail : $userUID")
            }
        }
        recentSearchList.add(searchWord)
        return recentSearchList
    }

    override fun removeRecentSearchItem(searchWord: String): List<String> {
        recentSearchList.remove(searchWord)

        //userID가 존재하면 firebase에서 삭제
        if (userUID != null) {
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("User").document(userUID).collection("recentSearch")
            val query = ref.whereEqualTo("searchWord", searchWord)

            query.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }
            }.addOnSuccessListener {
                Log.d("searchFirebase", "is success : $searchWord")
            }.addOnFailureListener {
                Log.d("searchFirebase", "is fail : $searchWord")
            }
        }
        return recentSearchList
    }
}