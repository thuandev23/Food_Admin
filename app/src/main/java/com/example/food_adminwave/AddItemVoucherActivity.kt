package com.example.food_adminwave

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.example.food_adminwave.databinding.ActivityAddItemVoucherBinding
import com.example.food_adminwave.model.AllVoucher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Random

class AddItemVoucherActivity : AppCompatActivity() {
    // Food item Details
    private var id: String? = null
    private lateinit var code: String
    private lateinit var description: String
    private lateinit var discountAmount: String
    private lateinit var discountPercent: String
    private lateinit var expiryDate: String
    private lateinit var minPurchaseAmount: String
    private lateinit var maxDiscount: String

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemVoucherBinding by lazy {
        ActivityAddItemVoucherBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        binding.imgBtnBack.setOnClickListener {
            finish()
        }

        binding.btnAddItem.setOnClickListener {
            val day = binding.addExpiryDate.dayOfMonth
            val month = binding.addExpiryDate.month + 1
            val year = binding.addExpiryDate.year

            expiryDate = "$day/$month/$year"
            code = binding.addCode.text.toString().trim()
            description = binding.addDescription.text.toString().trim()
            discountAmount = binding.addDiscountAmount.text.toString().trim()
            discountPercent = binding.addDiscountPercent.text.toString().trim()
            expiryDate = "$day/$month/$year"
            minPurchaseAmount = binding.addMinPurchaseAmount.text.toString().trim()
            maxDiscount = binding.addMaxDiscount.text.toString().trim()


            if (!(code.isBlank() || description.isBlank() || discountAmount.isBlank() ||
                        discountPercent.isBlank() || expiryDate.isBlank() ||
                        minPurchaseAmount.isBlank() || maxDiscount.isBlank())
            ) {
                upLoadData()
                Toast.makeText(this, "New Voucher added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun upLoadData() {
        val voucherRef = database.getReference("voucher")
        id = voucherRef.push().key

        if (id != null) {
            val newVoucher = AllVoucher(
                id,
                code,
                description,
                discountAmount,
                discountPercent,
                expiryDate,
                minPurchaseAmount,
                maxDiscount,
                false
            )
            newVoucher.let {
                voucherRef.child(id!!).setValue(it).addOnSuccessListener {
                    Toast.makeText(this, "Data upload successfully", Toast.LENGTH_SHORT)
                        .show()
                }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Data upload failed: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        } else {
            Toast.makeText(this, "Failed to generate a unique key", Toast.LENGTH_SHORT)
                .show()
        }
    }

}