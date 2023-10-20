package com.nbcamp_14_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.nbcamp_14_project.home.Home
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.ui.login.LoginViewModel

class FavoriteFragment: Fragment() {
    companion object{
        fun newInstance() = Home()
    }
    private var _binding: FragmentFavoriteBinding? = null
    private val viewModel: LoginViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNick.text = viewModel.userLiveData.toString()
    }

}