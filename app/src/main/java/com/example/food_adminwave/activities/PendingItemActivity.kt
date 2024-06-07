package com.example.food_adminwave.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_adminwave.adapter.PendingItemAdapter
import com.example.food_adminwave.databinding.ActivityPendingItemBinding
import com.example.food_adminwave.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingItemActivity : AppCompatActivity(), PendingItemAdapter.OnItemClicked {
    private lateinit var binding: ActivityPendingItemBinding
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfToTalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfAddress: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            finish()
        }
        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")
        getOrderDetails()
    }

    private fun getOrderDetails() {
        databaseOrderDetails.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfOrderItem.clear()
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataListForRecyclerView()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PendingItemActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addDataListForRecyclerView() {
        for (orderItem in listOfOrderItem) {
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfToTalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstFoodOrder.add(it)
            }
            orderItem.address   ?.let { listOfAddress.add(it) }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter =
            PendingItemAdapter(this, listOfName, listOfToTalPrice, listOfImageFirstFoodOrder, listOfAddress, this)
        binding.pendingOrdersRecyclerView.adapter = adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
        // handle item acceptance and update database
        database = FirebaseDatabase.getInstance()
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
            ?.addOnSuccessListener {
                updateOrderAcceptStatus(position)
            }?.addOnFailureListener {
                Toast.makeText(this, "Order is not Accepted", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onItemDispatchClickListener(position: Int) {
        // handle item dispatch and update database
        database = FirebaseDatabase.getInstance()
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference =
            database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deletedThisItemFromOrderDetails(dispatchItemPushKey)
            }.addOnFailureListener {
                Toast.makeText(this, "Order is not Dispatch", Toast.LENGTH_SHORT).show()
            }

    }

    private fun deletedThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsItemReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemReference.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Order is Dispatch", Toast.LENGTH_SHORT).show()        
        }.addOnFailureListener {
            Toast.makeText(this, "Order is not Dispatch", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateOrderAcceptStatus(position: Int) {
        //update order acceptance in user's BuyHistory and OrderDetails
        val userIdOfClickedItem = listOfOrderItem[position].userUId
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        if (userIdOfClickedItem != null && pushKeyOfClickedItem != null) {
            val historyReference = database.reference.child("accounts")
                    .child("users").child(userIdOfClickedItem)
                    .child("BuyHistory").child(pushKeyOfClickedItem)
            historyReference.child("orderAccepted").setValue(true)
            databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)
        }else{
            Toast.makeText(this, "User ID or Push Key is null ", Toast.LENGTH_SHORT).show()
        }
    }
}
