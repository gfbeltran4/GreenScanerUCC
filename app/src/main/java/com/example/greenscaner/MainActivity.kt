package com.example.greenscaner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.greenscaner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btn: Button = findViewById(R.id.boton1)
        btn.setOnClickListener {
            val intent: Intent = Intent(this, GreenScaner::class.java)
            startActivity(intent)
        }
    }
}



