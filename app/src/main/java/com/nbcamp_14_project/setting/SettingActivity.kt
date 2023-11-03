package com.nbcamp_14_project.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
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

            referenceMail.setOnClickListener {
                val intent = Intent(this@SettingActivity, EmailFormActivity::class.java)
                startActivity(intent)
            }

            openSourceLicence.setOnClickListener {
                OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스 목록")
                startActivity(Intent(this@SettingActivity, OssLicensesMenuActivity::class.java))
            }
        }
    }
}