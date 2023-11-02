package com.nbcamp_14_project.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nbcamp_14_project.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btnSettings()

    }

    private fun btnSettings() {
        with(binding) {
            settingBackBtn.setOnClickListener {
                finish()
            }
        }
    }
}