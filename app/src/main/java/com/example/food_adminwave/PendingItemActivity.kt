package com.example.food_adminwave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_adminwave.adapter.PendingItemAdapter
import com.example.food_adminwave.databinding.ActivityPendingItemBinding

class PendingItemActivity : AppCompatActivity() {
    private val binding : ActivityPendingItemBinding by lazy {
        ActivityPendingItemBinding.inflate(layoutInflater)
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

        val adapter = PendingItemAdapter(ArrayList(menuFoodName), ArrayList(menuItemPrice), ArrayList(menuImage), this)
        binding.pendingOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pendingOrdersRecyclerView.adapter = adapter
    }
}