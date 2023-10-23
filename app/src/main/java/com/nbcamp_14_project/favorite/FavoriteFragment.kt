package com.nbcamp_14_project.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.nbcamp_14_project.data.model.User
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.mainpage.MainActivity
import com.nbcamp_14_project.ui.login.LoginViewModel

class FavoriteFragment : Fragment() {
    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteListAdapter
    private val viewModel: FavoriteViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteListAdapter { item ->
            val detailInfo = item
            detailViewModel.setDetailInfo(detailInfo)
            val mainActivity = (activity as MainActivity)
            mainActivity.test()
        }



        binding.favoriteList.layoutManager = LinearLayoutManager(context)
        binding.favoriteList.adapter = adapter
        viewModel.favoriteList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        if (FirebaseAuth.getInstance().currentUser != null) {
            val collectionRef = firestore.collection("User").document(uid!!)
            collectionRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        val nameField = document.getString("name")
                        val emailField = document.getString("email")
                        binding.tvNick.text = "이름: $nameField"

                    } else {
                        Log.d("data", "no data")
                    }
                } else {
                    Log.d("data", "no data")
                }
            }


        }
    }
    
}
