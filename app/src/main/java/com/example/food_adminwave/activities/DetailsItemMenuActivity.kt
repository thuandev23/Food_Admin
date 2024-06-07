package com.example.food_adminwave.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.food_adminwave.databinding.ActivityDetailsItemMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsItemMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsItemMenuBinding
    private var foodId: String? = null
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodDescription: String? = null
    private var foodImage: String? = null
    private var foodIngredient: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsItemMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        binding.btnBackDetails.setOnClickListener {
            finish()
        }
        foodId = intent.getStringExtra("menuItemId")
        foodName = intent.getStringExtra("menuItemName")
        foodPrice = intent.getStringExtra("menuItemPrice")
        foodDescription = intent.getStringExtra("menuItemDescription")
        foodImage = intent.getStringExtra("menuItemImage")
        foodIngredient = intent.getStringExtra("menuItemIngredient")
        with(binding) {
            foodNameDetails.text = foodName?.toEditable()
            foodDescriptionDetails.text = foodDescription?.toEditable()
            foodIngredientDetails.text = foodIngredient?.toEditable()
            foodPriceDetails.text = foodPrice?.toEditable()
            Glide.with(this@DetailsItemMenuActivity).load(Uri.parse(foodImage))
                .into(foodImageDetails)
        }

        binding.foodNameDetails.isEnabled = false
        binding.foodDescriptionDetails.isEnabled = false
        binding.foodIngredientDetails.isEnabled = false
        binding.foodPriceDetails.isEnabled = false
        binding.textView28.isEnabled = false
        var isEnable = false
        binding.clickEditButton.setOnClickListener {
            isEnable = !isEnable
            binding.foodNameDetails.isEnabled = isEnable
            binding.foodDescriptionDetails.isEnabled = isEnable
            binding.foodIngredientDetails.isEnabled = isEnable
            binding.foodPriceDetails.isEnabled = isEnable
            binding.textView28.isEnabled = isEnable
            if (isEnable) {
                binding.foodNameDetails.requestFocus()
            }
        }

        binding.btnSaveItem.setOnClickListener {
            foodName = binding.foodNameDetails.text.toString().trim()
            foodDescription = binding.foodDescriptionDetails.text.toString().trim()
            foodPrice = binding.foodPriceDetails.text.toString().trim()
            foodIngredient = binding.foodIngredientDetails.text.toString().trim()

            if (!(foodName!!.isBlank() || foodDescription!!.isBlank() || foodPrice!!.isBlank() ||
                        foodIngredient!!.isBlank())
            ) {
                updateItem()
                finish()
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateItem() {
        val menuRef = database.getReference("menu").child(foodId!!)
        val updateItem = hashMapOf(
            "key" to foodId,
            "foodName" to binding.foodNameDetails.text.toString(),
            "foodPrice" to binding.foodPriceDetails.text.toString(),
            "foodDescription" to binding.foodDescriptionDetails.text.toString(),
            "foodImage" to foodImage,
            "foodIngredient" to binding.foodIngredientDetails.text.toString(),
        )
        menuRef.setValue(updateItem).addOnSuccessListener {
            Toast.makeText(this, "Data upload successfully", Toast.LENGTH_SHORT)
                .show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "Data upload failed: ${it.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}