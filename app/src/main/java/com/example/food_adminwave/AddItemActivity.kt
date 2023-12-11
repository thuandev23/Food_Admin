package com.example.food_adminwave

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.food_adminwave.databinding.ActivityAddItemBinding
import com.example.food_adminwave.model.AllItemMenu
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    // Food item Details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private var foodImage: Uri? = null
    private lateinit var foodIngredient: String

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
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
            foodName = binding.addFoodName.text.toString().trim()
            foodPrice = binding.addFoodPrice.text.toString().trim()
            foodDescription = binding.addFoodDescription.text.toString().trim()
            foodIngredient = binding.addFoodIngredients.text.toString().trim()
            if (!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredient.isBlank())) {
                uploadData()
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun uploadData() {
        // get a reference to the "menu" node in the database
        val menuRef = database.getReference("menu")
        // Generate a unique key for the new menu item
        val newItemKey = menuRef.push().key

        if (foodImage != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_image/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImage!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnCompleteListener { downLoad ->
                    // create new menu item
                    if (newItemKey != null) {
                        val newItem = AllItemMenu(
                            newItemKey,
                            foodName = foodName,
                            foodPrice = foodPrice,
                            foodDescription = foodDescription,
                            foodIngredient = foodIngredient,
                            foodImage = downLoad.result.toString()
                        )
                        newItem.let {
                            menuRef.child(newItemKey).setValue(it).addOnSuccessListener {
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

            }.addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectImage.setImageURI(uri)
            foodImage = uri
        }
    }
}