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
    private val usersReference = database.reference.child("user")

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
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false
        binding.showPassword.isEnabled = false
        var isEnable = false
        binding.clickEditButton.setOnClickListener {
            isEnable =! isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            binding.showPassword.isEnabled = isEnable
            if (isEnable){
                binding.name.requestFocus()
            }
        }
        fetchUserData()
    }

    private fun togglePasswordVisibility(passwordVisible: Boolean) {
        if (passwordVisible){
            binding.password.transformationMethod = null
        }
        else{
            binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun fetchUserData() {
        val currentUser = auth.currentUser
        val userId =  currentUser?.uid
        if(userId != null){
            usersReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val user = snapshot.getValue(UserModel::class.java)

                        binding.name.setText(user?.name)
                        binding.address.setText(user?.address)
                        binding.email.setText(user?.email)
                        binding.phone.setText(user?.phone ?: "Please add you phone")
                        binding.password.setText(user?.password)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("DatabaseUserError", "Error: ${error.message}")
                }

            })
        }
    }
}