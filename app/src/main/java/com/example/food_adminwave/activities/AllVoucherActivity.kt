package com.example.food_adminwave.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_adminwave.adapter.VoucherAdapter
import com.example.food_adminwave.databinding.ActivityAllVoucherBinding
import com.example.food_adminwave.model.AllVoucher
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllVoucherActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var voucherItems: ArrayList<AllVoucher> = ArrayList()

    private val binding: ActivityAllVoucherBinding by lazy {
        ActivityAllVoucherBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            finish()
        }

        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveVoucherItem()
    }

    private fun retrieveVoucherItem() {
        database = FirebaseDatabase.getInstance()
        val voucherRef: DatabaseReference = database.reference.child("voucher")
        voucherRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                voucherItems.clear()
                val voucherItemsCount = snapshot.childrenCount
                binding.quantityAllVoucher.text = "Quantity: $voucherItemsCount"
                for (voucherSnapshot in snapshot.children) {
                    val voucherItem = voucherSnapshot.getValue(AllVoucher::class.java)
                    voucherItem?.let { voucherItems.add(it) }
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
            VoucherAdapter(this@AllVoucherActivity, voucherItems, databaseReference) { position ->
                deleteVoucher(position)
            }
        binding.voucherAllItemRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.voucherAllItemRecyclerView.adapter = adapter
    }

    private fun deleteVoucher(position: Int) {
        voucherItems.removeAt(position)
        binding.voucherAllItemRecyclerView.adapter?.notifyItemRemoved(position)
    }
}