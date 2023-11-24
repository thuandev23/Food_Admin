package com.example.food_adminwave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.food_adminwave.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private val binding : ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.selectImage.setOnClickListener{
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.imgBtnBack.setOnClickListener {
            finish()
        }

        binding.btnAddItem.setOnClickListener {  }
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        uri ->
        if (uri != null){
            binding.selectImage.setImageURI(uri)
        }
    }
}