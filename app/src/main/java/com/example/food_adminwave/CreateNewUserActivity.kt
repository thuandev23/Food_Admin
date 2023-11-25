package com.example.food_adminwave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.food_adminwave.databinding.ActivityCreateNewUserBinding

class CreateNewUserActivity : AppCompatActivity() {
    private val binding: ActivityCreateNewUserBinding by lazy {
        ActivityCreateNewUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            finish()
        }
        binding.btnCreateNewUser.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}