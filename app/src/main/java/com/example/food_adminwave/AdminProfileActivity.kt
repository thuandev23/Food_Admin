package com.example.food_adminwave

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.example.food_adminwave.databinding.ActivityAdminProfileBinding
import com.example.food_adminwave.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {

    // Initialize Firebase components
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            finish()
        }

        binding.btnSaveInformation.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        var isPasswordVisible = false
        binding.showPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(isPasswordVisible)
        }

        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.nameRestaurant.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false
        binding.showPassword.isEnabled = false
        var isEnable = false
        binding.clickEditButton.setOnClickListener {
            isEnable = !isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.nameRestaurant.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            binding.showPassword.isEnabled = isEnable
            if (isEnable) {
                binding.name.requestFocus()
            }
        }
        fetchUserData()
    }

    private fun togglePasswordVisibility(passwordVisible: Boolean) {
        if (passwordVisible) {
            binding.password.transformationMethod = null
        } else {
            binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)
            userReference.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    if (user!= null) {
                        binding.name.setText(user?.name ?: "Please add you name")
                        binding.address.setText(user?.address ?: "Please add you address")
                        binding.nameRestaurant.setText(user?.nameOfRestaurant ?: "Please add you name of restaurant")
                        binding.email.setText(user?.email ?: "Please add you email")
                        binding.phone.setText(user?.phone ?: "Please add you phone")
                        binding.password.setText(user?.password ?: "Please add you password")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("DatabaseUserError", "Error: ${error.message}")
                }

            })
        }
    }
}