package com.nbcamp_14_project.ui.login

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import com.nbcamp_14_project.databinding.CategoryDialogBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CategoryDialog(
    context: Context
) : Dialog(context) {

    private lateinit var binding: CategoryDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 만들어놓은 dialog_profile.xml 뷰를 띄운다.
        binding = CategoryDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

    }

    override fun dismiss() {
        super.dismiss()
        ownerActivity?.finish()
    }

    private fun initViews() = with(binding) {
        // 뒤로가기 버튼, 빈 화면 터치를 통해 dialog가 사라지지 않도록한다
        setCancelable(false)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnSocial.setOnClickListener {

        }

        btnComplete.setOnClickListener {
            dismiss()
        }
    }

}