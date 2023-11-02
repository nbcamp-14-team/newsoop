package com.nbcamp_14_project.newspaper

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nbcamp_14_project.databinding.FragmentNewspaperBinding
import com.nbcamp_14_project.ui.login.LoginActivity

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
                startActivity(NewspaperDialog.newInstance(requireContext(),link))
            }
        )
    }
    private val economyAdapter by lazy{
        NewsPaperAdapter(
            onClick = {link ->
                NewspaperDialog.newInstance(requireContext(),link)
            }
        )
    }
    private val societyAdapter by lazy{
        NewsPaperAdapter(
            onClick = {link ->
                NewspaperDialog.newInstance(requireContext(),link)
            }
        )
    }
    private val lifeAdapter by lazy{
        NewsPaperAdapter(
            onClick = {link ->
                NewspaperDialog.newInstance(requireContext(),link)
            }
        )
    }
    private val cultureAdapter by lazy{
        NewsPaperAdapter(
            onClick = {link ->
                NewspaperDialog.newInstance(requireContext(),link)
            }
        )
    }
    private val itAdapter by lazy{
        NewsPaperAdapter(
            onClick = {link ->
                NewspaperDialog.newInstance(requireContext(),link)
            }
        )
    }
    private val scienceAdapter by lazy{
        NewsPaperAdapter(
            onClick = {link ->
                NewspaperDialog.newInstance(requireContext(),link)
            }
        )
    }
    private val worldAdapter by lazy{
        NewsPaperAdapter(
            onClick = {link ->
                NewspaperDialog.newInstance(requireContext(),link)
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
        btnPolitics.setOnClickListener {
            rvNewspaper.adapter = politicsAdapter
        }
        btnEconomy.setOnClickListener {
            rvNewspaper.adapter = economyAdapter
        }
        btnSociety.setOnClickListener {
            rvNewspaper.adapter = societyAdapter
        }
        btnLife.setOnClickListener {
            rvNewspaper.adapter = lifeAdapter
        }
        btnCulture.setOnClickListener {
            rvNewspaper.adapter = cultureAdapter
        }
        btnIt.setOnClickListener {
            rvNewspaper.adapter = itAdapter
        }
        btnScience.setOnClickListener {
            rvNewspaper.adapter = scienceAdapter
        }
        btnWorld.setOnClickListener {
            rvNewspaper.adapter = worldAdapter
        }
    }
    fun initViewModel(){
        with(newspaperViewModel){
            politicsList.observe(viewLifecycleOwner){
                politicsAdapter.submitList(it)
            }
            economyList.observe(viewLifecycleOwner){
                economyAdapter.submitList(it)
            }
            societyList.observe(viewLifecycleOwner){
                societyAdapter.submitList(it)
            }
            lifeList.observe(viewLifecycleOwner){
                lifeAdapter.submitList(it)
            }
            cultureList.observe(viewLifecycleOwner){
                cultureAdapter.submitList(it)
            }
            itList.observe(viewLifecycleOwner){
                itAdapter.submitList(it)
            }
            scienceList.observe(viewLifecycleOwner){
                scienceAdapter.submitList(it)
            }
            worldList.observe(viewLifecycleOwner){
                worldAdapter.submitList(it)
            }
        }
    }

}

