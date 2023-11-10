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
    fun clearRecentSearchList(): List<String>
    fun setRecentSearchItem(searchWord: String): List<String>
}

class SearchRepositoryImpl(
    private val idGenerate: AtomicInteger,
    private val remoteDatasource: NewsCollector
) : SearchRepository {
    private val searchList = mutableListOf<HomeModel>()
    private val recentSearchList = mutableListOf<String>()

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
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid
        if (userUID != null) {
            val db = FirebaseFirestore.getInstance()
            val recentSearchCollection =
                db.collection("User").document(userUID).collection("recentSearch")
            recentSearchCollection.orderBy("inputTime").get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        val searchWord = document.getString("searchWord")
                        if (searchWord != null) {
                            recentSearchList.add(searchWord)
                            Log.d("recentSearch", "$searchWord")
                        }
                    }
                    Log.d("recentSearchFirebase", "is success")
                }
                .addOnFailureListener { e ->
                    Log.d("recentSearchFirebase", "is fail")
                }
        }
        return recentSearchList
    }

    override fun addRecentSearchList(searchWord: String): List<String> {
        if (searchWord == "") {
            return recentSearchList
        }

        //최근 검색어가 5개 넘어가면 처음 입력값 지우기
        if (recentSearchList.size >= 5) {
            removeRecentSearchItem(recentSearchList[0])
            Log.d("recentSearchRemove10over", recentSearchList[0])
        }

        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid

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
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid

        //같은 단어가 있는지 체크하기
        var checkWord = recentSearchList.contains(searchWord)

        //userID가 존재하면 firebase에서 삭제
        if (userUID != null && checkWord) {
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

        recentSearchList.remove(searchWord)
        return recentSearchList
    }

    override fun clearRecentSearchList(): List<String> {
        recentSearchList.clear()
        return ArrayList<String>(recentSearchList)
    }

    override fun setRecentSearchItem(searchWord: String): List<String> {
        if (searchWord == "") {
            return recentSearchList
        }
        //최근 검색어가 5개 넘어가면 처음 입력값 지우기
        if (recentSearchList.size >= 5) {
            removeRecentSearchItem(recentSearchList[0])
            Log.d("recentSearchRemove10over", recentSearchList[0])
        }
        recentSearchList.add(searchWord)
        return recentSearchList
    }
}