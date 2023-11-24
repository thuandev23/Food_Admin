package com.example.food_adminwave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_adminwave.adapter.AddItemAdapter
import com.example.food_adminwave.databinding.ActivityAllItemBinding

class AllItemActivity : AppCompatActivity() {

    private val binding : ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            finish()
        }

        val menuFoodName = listOf("Burger", "Sandwich", "momo", "item", "check profile","Sandwich", "momo", "item", "check profile")
        val menuItemPrice = listOf("$5", "$6", "$7", "$8", "$9", "$6", "$7", "$8", "$9")
        val menuImage = listOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,R.drawable.menu4,R.drawable.menu5,R.drawable.menu2,R.drawable.menu3,R.drawable.menu4,R.drawable.menu5)

        val adapter = AddItemAdapter(ArrayList(menuFoodName), ArrayList(menuItemPrice), ArrayList(menuImage))
        binding.menuAllItemRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuAllItemRecyclerView.adapter = adapter
    }
}