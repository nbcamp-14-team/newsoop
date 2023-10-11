package com.nbcamp_14_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    init{
        instance = this
    }
    companion object{
        private var instance: MainActivity? = null
        fun newInstance(): MainActivity?{
            return instance
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    private val binding by lazy{ }

}