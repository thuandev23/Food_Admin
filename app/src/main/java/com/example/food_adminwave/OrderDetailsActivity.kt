package com.example.food_adminwave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_adminwave.adapter.OrderDetailsAdapter
import com.example.food_adminwave.adapter.PendingItemAdapter
import com.example.food_adminwave.databinding.ActivityOrderDetailsBinding
import com.example.food_adminwave.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding : ActivityOrderDetailsBinding by lazy{
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var foodNames:ArrayList<String>  = arrayListOf()
    private var foodImages:ArrayList<String>  = arrayListOf()
    private var foodQuantity:ArrayList<Int>  = arrayListOf()
    private var foodPrices:ArrayList<String>  = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnBackPayout.setOnClickListener {
            finish()
        }

        getOrderDetails()
    }

    private fun getOrderDetails() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails?.let {
            orderDetails ->

        if (receivedOrderDetails != null){
            userName = receivedOrderDetails.userName
            address = receivedOrderDetails.address
            phoneNumber = receivedOrderDetails.phoneNumber
            totalPrice = receivedOrderDetails.totalPrice
            foodNames = receivedOrderDetails.foodNames as ArrayList<String>
            foodImages =receivedOrderDetails.foodImages as ArrayList<String>
            foodPrices = receivedOrderDetails.foodPrices as ArrayList<String>
            foodQuantity = receivedOrderDetails.foodQuantities as ArrayList<Int>

            setUserDetail()
            setAdapter()
        }
        }
    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter =
            OrderDetailsAdapter(this, foodNames, foodImages, foodQuantity, foodPrices)
        binding.orderDetailsRecyclerView.adapter = adapter
    }

    private fun setUserDetail() {
        binding.name.text = userName
        binding.address.text = address
        binding.phone.text = phoneNumber
        binding.totalPay.text = totalPrice
    }
}