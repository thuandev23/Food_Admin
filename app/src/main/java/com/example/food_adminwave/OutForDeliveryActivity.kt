package com.example.food_adminwave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_adminwave.adapter.DeliveryAdapter
import com.example.food_adminwave.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            finish()
        }

        val customerName = arrayListOf(
            "John Doe", "Jame Smith", "Mike JohnSon"
        )
        val moneyStatus = arrayListOf(
            "received", "notReceived", "Pending"
        )
        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deliveryRecyclerView.adapter  = adapter
    }

}