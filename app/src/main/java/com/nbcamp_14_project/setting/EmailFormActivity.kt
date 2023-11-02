package com.nbcamp_14_project.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nbcamp_14_project.databinding.ActivityEmailFormBinding

class EmailFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendEmailBtn.setOnClickListener {
            val content =
                binding.sendEmailText.text.toString()
            sendEmail(content)
        }

    }

    private fun sendEmail(content: String) {
        val email = Intent(Intent.ACTION_SEND)
        email.type = "plain/text"
        val address = arrayOf("hyunsik0923@gmail.com")
        email.putExtra(Intent.EXTRA_EMAIL, address)
        email.putExtra(Intent.EXTRA_SUBJECT, "newsoop 문의사항")
        email.putExtra(Intent.EXTRA_TEXT, content)
        startActivity(email)
    }
}