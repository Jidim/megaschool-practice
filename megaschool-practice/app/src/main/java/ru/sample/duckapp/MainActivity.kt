package ru.sample.duckapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDucks1ButtonClick(view: android.view.View) {
        val intent = Intent(this, Ducks1Activity::class.java)
        startActivity(intent)
    }
}