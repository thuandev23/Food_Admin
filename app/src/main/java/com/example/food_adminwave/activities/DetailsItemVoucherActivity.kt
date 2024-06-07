package com.example.food_adminwave.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.example.food_adminwave.databinding.ActivityDetailsItemVoucherBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsItemVoucherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsItemVoucherBinding
    private var voucherId: String? = null
    private var voucherCode: String? = null
    private var voucherDescription: String? = null
    private var voucherDiscountAmount: String? = null
    private var voucherDiscountPercent: String? = null
    private var voucherExpiryDate: String? = null
    private var voucherMinPurchaseAmount: String? = null
    private var voucherMaxDiscount: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsItemVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        binding.btnBackDetails.setOnClickListener {
            finish()
        }

        voucherId = intent.getStringExtra("voucherItemID")
        voucherCode = intent.getStringExtra("voucherItemCode")
        voucherDescription = intent.getStringExtra("voucherItemDescription")
        voucherDiscountAmount = intent.getStringExtra("voucherItemDiscountAmount")
        voucherDiscountPercent = intent.getStringExtra("voucherItemDiscountPercent")
        voucherExpiryDate = intent.getStringExtra("voucherItemExpiryDate")
        voucherMinPurchaseAmount = intent.getStringExtra("voucherItemMinPurchaseAmount")
        voucherMaxDiscount = intent.getStringExtra("voucherItemMaxDiscount")

        with(binding) {
            code.text = voucherCode?.toEditable()
            description.text = voucherDescription?.toEditable()
            discountAmount.text = voucherDiscountAmount?.toEditable()
            discountPercent.text = voucherDiscountPercent?.toEditable()
            expiryDate.text = voucherExpiryDate?.toEditable()
            minPurchaseAmount.text = voucherMinPurchaseAmount?.toEditable()
            maxDiscount.text = voucherMaxDiscount?.toEditable()
        }

        binding.code.isEnabled = false
        binding.description.isEnabled = false
        binding.discountAmount.isEnabled = false
        binding.discountPercent.isEnabled = false
        binding.expiryDate.isEnabled = false
        binding.minPurchaseAmount.isEnabled = false
        binding.maxDiscount.isEnabled = false

        var isEnable = false
        binding.clickEditButton.setOnClickListener {
            isEnable = !isEnable
            binding.code.isEnabled = isEnable
            binding.description.isEnabled = isEnable
            binding.discountAmount.isEnabled = isEnable
            binding.discountPercent.isEnabled = isEnable
            binding.expiryDate.isEnabled = isEnable
            binding.minPurchaseAmount.isEnabled = isEnable
            binding.maxDiscount.isEnabled = isEnable
            if (isEnable) {
                binding.code.requestFocus()
            }
        }

        binding.btnSaveItem.setOnClickListener {
            voucherCode = binding.code.text.toString().trim()
            voucherDescription = binding.description.text.toString().trim()
            voucherDiscountAmount = binding.discountAmount.text.toString().trim()
            voucherDiscountPercent = binding.discountPercent.text.toString().trim()
            voucherExpiryDate = binding.expiryDate.text.toString().trim()
            voucherMinPurchaseAmount = binding.minPurchaseAmount.text.toString().trim()
            voucherMaxDiscount = binding.maxDiscount.text.toString().trim()


            if (!(voucherCode!!.isBlank() || voucherDescription!!.isBlank() || voucherDiscountAmount!!.isBlank() ||
                        voucherDiscountPercent!!.isBlank() || voucherExpiryDate!!.isBlank() ||
                        voucherMinPurchaseAmount!!.isBlank() || voucherMaxDiscount!!.isBlank())
            ) {
                updateVoucher()
                finish()
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateVoucher() {
        val voucherRef = database.getReference("voucher").child(voucherId!!)
        val updateVoucher = hashMapOf(
            "id" to voucherId,
            "code" to binding.code.text.toString(),
            "description" to binding.description.text.toString(),
            "discountAmount" to binding.discountAmount.text.toString(),
            "discountPercent" to binding.discountPercent.text.toString(),
            "expiryDate" to binding.expiryDate.text.toString(),
            "minPurchaseAmount" to binding.minPurchaseAmount.text.toString(),
            "maxDiscount" to binding.maxDiscount.text.toString(),
            "isUsed" to false
        )

        voucherRef.setValue(updateVoucher).addOnSuccessListener {
            Toast.makeText(this, " Voucher update successfully", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Data update failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }

    }
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}