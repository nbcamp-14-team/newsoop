package com.nbcamp_14_project.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentCategoryBinding
import com.nbcamp_14_project.databinding.FragmentFixPwBinding


class FixPwFragment : Fragment() {

    private var _binding: FragmentFixPwBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var fbFireStore: FirebaseFirestore
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFixPwBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        fbFireStore = FirebaseFirestore.getInstance()
        etEmailTextWatcher()
        binding.btnFixComplete.setOnClickListener {
            sendEmail()
        }
        binding.ivResetPwBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun sendEmail() {
        val email = binding.etEmail.text.toString()
        val pattern = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (pattern) {
            fbFireStore.collection("User")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val querySnapshot = task.result
                        if (querySnapshot != null && !querySnapshot.isEmpty) {
                            // 중복된 이메일이 이미 존재함
                            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("email", "good")
                                    Toast.makeText(
                                        requireContext(),
                                        "메일이 전송되었습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    parentFragmentManager.popBackStack()
                                }

                            }

                        } else {
                            Toast.makeText(requireContext(), "가입된 이메일이 아닙니다.", Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                }

        } else {

            Log.e("email", "bad")
        }
    }

    private fun etEmailTextWatcher() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = binding.etEmail.text.toString()
                val pattern = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                binding.etEmail.requestFocus()
                if (pattern) {
                    binding.btnFixComplete.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.signUpBtn
                        )
                    )
                } else {
                    binding.btnFixComplete.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.lightGray
                        )
                    )
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun checkEmail() {
        val emailToCheck = binding.etEmail.text.toString()
        fbFireStore.collection("User")
            .whereEqualTo("email", emailToCheck)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        // 중복된 이메일이 이미 존재함


                    } else {


                    }
                }
            }
    }


}