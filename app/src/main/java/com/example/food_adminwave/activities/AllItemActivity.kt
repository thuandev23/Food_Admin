package com.example.food_adminwave.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_adminwave.adapter.MenuItemAdapter
import com.example.food_adminwave.databinding.ActivityAllItemBinding
import com.example.food_adminwave.model.AllItemMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems: ArrayList<AllItemMenu> = ArrayList()

    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imgBtnBack.setOnClickListener {
            finish()
        }
        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItem()
    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")

        foodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                val menuItemsCount = snapshot.childrenCount
                binding.quantityAllFood.text = "Quantity: $menuItemsCount"
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(AllItemMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError", "Error: ${error.message}")
            }
        })
    }

    private fun setAdapter() {
        val adapter =
            MenuItemAdapter(this@AllItemActivity, menuItems, databaseReference) { position ->
                deleteMenuItems(position)
            }
        binding.menuAllItemRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuAllItemRecyclerView.adapter = adapter
    }

    private fun deleteMenuItems(position: Int) {
        menuItems.removeAt(position)
        binding.menuAllItemRecyclerView.adapter?.notifyItemRemoved(position)
    }
}