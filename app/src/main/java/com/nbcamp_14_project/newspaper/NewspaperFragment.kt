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

class NewspaperFragment : Fragment() {

    private var _binding: FragmentNewspaperBinding? = null
    private val binding get() = _binding!!
    private val newspaperViewModel: NewspaperViewModel by lazy {
        ViewModelProvider(
            this, NewspaperModelFactory()
        )[NewspaperViewModel::class.java]
    }
    private val currentListAdapter by lazy {
        NewsPaperAdapter(
            onClick = { link ->
                startActivity(NewspaperDialog.newInstance(requireContext(), link))
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

    fun initView() = with(binding) {
        newspaperViewModel.getCurrentList("정치")
        rvNewspaper.adapter = currentListAdapter
        rvNewspaper.layoutManager = GridLayoutManager(requireContext(), 2)
        btnPolitics.setOnClickListener {
            newspaperViewModel.getCurrentList("정치")
        }
        btnEconomy.setOnClickListener {
            newspaperViewModel.getCurrentList("경제")
        }
        btnSociety.setOnClickListener {
            newspaperViewModel.getCurrentList("사회")
        }
        btnLife.setOnClickListener {
            newspaperViewModel.getCurrentList("생활")
        }
        btnCulture.setOnClickListener {
            newspaperViewModel.getCurrentList("문화")
        }
        btnIt.setOnClickListener {
            newspaperViewModel.getCurrentList("IT")
        }
        btnScience.setOnClickListener {
            newspaperViewModel.getCurrentList("과학")
        }
        btnWorld.setOnClickListener {
            newspaperViewModel.getCurrentList("세계")
        }
    }

    fun initViewModel() {
        with(newspaperViewModel) {
            currentList.observe(viewLifecycleOwner) {
                currentListAdapter.submitList(it)
            }

        }
    }

}

