package com.nbcamp_14_project.newspaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nbcamp_14_project.databinding.FragmentNewspaperBinding

class NewspaperFragment:Fragment() {

    private var _binding: FragmentNewspaperBinding? = null
    private val binding get() = _binding!!
    private val newspaperViewModel:NewspaperViewModel by lazy{
        ViewModelProvider(
            this,NewspaperModelFactory()
        )[NewspaperViewModel::class.java]
    }
    private val politicsAdapter by lazy{
        NewsPaperAdapter(
            onClick = {link ->

            }
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewspaperBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()//화면 설정 함수
        initViewModel()

    }
    fun initView() = with(binding){
        rvNewspaper.adapter = politicsAdapter
        rvNewspaper.layoutManager=GridLayoutManager(requireContext(),2)
    }
    fun initViewModel(){
        with(newspaperViewModel){
            politicsList.observe(viewLifecycleOwner){
                politicsAdapter.submitList(it)
            }
        }
    }

}

