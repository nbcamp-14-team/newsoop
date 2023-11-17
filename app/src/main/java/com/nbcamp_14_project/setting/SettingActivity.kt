package com.nbcamp_14_project.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.nbcamp_14_project.databinding.ActivitySettingBinding
import com.nbcamp_14_project.newspaper.NewspaperDialog

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private var checkDeveloper = false

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

            terms.setOnClickListener {
                val link = "https://tall-button-e3d.notion.site/a16106c5e59d4985ae10f365a4a66d03"
                startActivity(NewspaperDialog.newInstance(this@SettingActivity, link))
            }

            developer.setOnClickListener {
                if(!checkDeveloper){
                    developerText.visibility = View.VISIBLE
                    checkDeveloper = true
                }else{
                    developerText.visibility = View.GONE
                    checkDeveloper = false
                }
            }
        }
    }
}