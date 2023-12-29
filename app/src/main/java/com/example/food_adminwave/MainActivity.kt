package com.example.food_adminwave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.food_adminwave.databinding.ActivityMainBinding
import com.example.food_adminwave.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completeOrderReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.addMenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.allItemMenu.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.addDiscount.setOnClickListener {
            val intent = Intent(this, AddItemVoucherActivity::class.java)
            startActivity(intent)
        }
        binding.allDiscount.setOnClickListener {
            val intent = Intent(this, AllVoucherActivity::class.java)
            startActivity(intent)
        }
        binding.outForDelivery.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.addNewUser.setOnClickListener {
            val intent = Intent(this, CreateNewUserActivity::class.java)
            startActivity(intent)
        }
        binding.btnPending.setOnClickListener {
            val intent = Intent(this, PendingItemActivity::class.java)
            startActivity(intent)
        }
        binding.logOut.setOnClickListener {
            if (auth.currentUser != null) {
                auth.addAuthStateListener { firebaseAuth ->
                    if (firebaseAuth.currentUser == null) {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }
                auth.signOut()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        pendingOrders()
        completeOrders()
        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        var listTotalPay = mutableListOf<Int>()
        completeOrderReference = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completeOrderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (order in snapshot.children) {
                    var completeOrder = order.getValue(OrderDetails::class.java)
                    completeOrder?.totalPrice?.replace("$", "")?.toIntOrNull()
                        ?.let { i ->
                            listTotalPay.add(i)
                        }
                }
                binding.totalPay.text = listTotalPay.sum().toString() + " VNĐ"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun completeOrders() {
        database = FirebaseDatabase.getInstance()
        var completeOrderReference = database.reference.child("CompletedOrder")
        var completeOrderItemCount = 0
        completeOrderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                completeOrderItemCount = snapshot.childrenCount.toInt()
                binding.completeOrders.text = completeOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun pendingOrders() {
        database = FirebaseDatabase.getInstance()
        var pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrders.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}