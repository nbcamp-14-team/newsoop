package com.nbcamp_14_project.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nbcamp_14_project.databinding.ActivityEmailFormBinding

class EmailFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailFormBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}