package com.nbcamp_14_project.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var fbFireStore: FirebaseFirestore
    private val loginViewModel: LoginViewModel by activityViewModels()
    private var greenCount = 0
    private val buttonStates = MutableList(8) { false }
    private val greenButtonTextList = mutableListOf<String>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        loginViewModel.updateBooleanValue(true)
        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCategoryBtn()

    }

    private fun setCategoryBtn() {
        binding.run {
            val buttons = listOf(
                btnPolitics,
                btnEconomy,
                btnSocial,
                btnLife,
                btnCulture,
                btnIt,
                btnScience,
                btnWorld
            )
            for (i in buttons.indices) {
                buttons[i].setOnClickListener {
                    if (buttonStates[i]) {
                        buttons[i].strokeColor = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.lightGray
                            )
                        )
                        val text = buttons[i].text.toString()
                        greenButtonTextList.remove(text)
                        buttonStates[i] = false
                        greenCount--
                    } else {
                        if (greenCount < 3) {
                            buttons[i].strokeColor = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.green
                                )
                            )
                            val text = buttons[i].text.toString()
                            greenButtonTextList.add(text)
                            buttonStates[i] = true
                            greenCount++
                        }

                    }
                }
                tvClose.setOnClickListener {
                    parentFragmentManager.popBackStack()

                }
                btnComplete.setOnClickListener {
                    putStringCategoryData()
                    parentFragmentManager.popBackStack()
                }
            }
        }

    }

    private fun putStringCategoryData(){
        loginViewModel.category.value = greenButtonTextList.getOrNull(0) ?: ""
        loginViewModel.secondCategory.value = greenButtonTextList.getOrNull(1) ?: ""
        loginViewModel.thirdCategory.value = greenButtonTextList.getOrNull(2) ?: ""

    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginViewModel.updateBooleanValue(false)
    }
}