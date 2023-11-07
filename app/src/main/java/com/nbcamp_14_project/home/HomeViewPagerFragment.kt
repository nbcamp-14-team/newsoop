package com.nbcamp_14_project.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nbcamp_14_project.databinding.FragmentViewpagerHomeBinding


class HomeViewPagerFragment : Fragment() {
    private var _binding: FragmentViewpagerHomeBinding? = null
    private val binding get() = _binding!!
    private val viewPagerAdapter by lazy {
        HomeViewPagerAdapter(requireActivity())
    }//runDetailFragment
//    private val viewModel: HomeViewPagerViewModel by lazy{
//        ViewModelProvider(
//            requireActivity(),HomeViewPagerViewModelFactory()
//        )[HomeViewPagerViewModel::class.java]
//    }
    private val viewModel: HomeViewPagerViewModel by activityViewModels{
        HomeViewPagerViewModelFactory()
    }
    private var isLogin = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewpagerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            initViewModel()

            if (FirebaseAuth.getInstance().currentUser != null ) {
                viewModel.addListAtFirst("추천","추천")
            }
            viewpager.adapter = viewPagerAdapter
            viewpager.run {
                isUserInputEnabled = false
            }
            viewpager.offscreenPageLimit = 2//생명주기 관련 코드
            TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
                tab.setText(viewPagerAdapter.getTitle(position))
            }.attach()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeView","aa")
        viewModel.refreshNews()
    }
    private fun initViewModel() {
        with(viewModel) {
            list.observe(viewLifecycleOwner) {
                viewPagerAdapter.setData(it)
            }
        }
    }
}

