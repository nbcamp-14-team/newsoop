package com.nbcamp_14_project.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var fbFireStore: FirebaseFirestore
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCategoryBtn()

    }

    private fun setCategoryBtn(){
        binding.run {
            val buttons = listOf(btnPolitics, btnEconomy, btnSocial,btnLife,btnCulture,btnIt,btnScience,btnWorld)
            for (button in buttons) {
                var switch = false
                button.setOnClickListener {
                    switch = !switch
                    if (switch){
                        button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.green))
                        val text = button.text.toString()
                        loginViewModel.category.value = text
                    }else{
                        button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))
                    }

                }
            }
            btnComplete.setOnClickListener {
                parentFragmentManager.popBackStack()

            }
        }
    }

}