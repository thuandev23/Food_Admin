package com.example.food_adminwave.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.example.food_adminwave.databinding.ActivitySignBinding
import com.example.food_adminwave.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignActivity : AppCompatActivity() {
    //firebase
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var address:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize Firebase Auth
        auth = Firebase.auth
        // initialize Firebase Database
        database = Firebase.database.reference

        var isPasswordVisible = false
        binding.showPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(isPasswordVisible)
        }
        binding.btnCreateAcc.setOnClickListener {
            // get text form edittext
            userName = binding.name.text.toString().trim()
            nameOfRestaurant = binding.nameRestaurant.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()
            address = binding.address.text.toString()
            if (userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank() || address.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                createNewAccount(email, password)
            }
        }
        binding.txtHasAcc.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.addAddress.setOnClickListener {
            Toast.makeText(this, "make location", Toast.LENGTH_SHORT).show()
        }
    }
    private fun togglePasswordVisibility(passwordVisible: Boolean) {
        if (passwordVisible){
            binding.password.transformationMethod = null
        }
        else{
            binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }
    private fun createNewAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account created failed: ${task.exception}", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createNewAccount: Failed", task.exception)
            }
        }
    }

    // save data in to database
    private fun saveUserData() {
        // get text form edittext
        userName = binding.name.text.toString().trim()
        nameOfRestaurant = binding.nameRestaurant.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()
        address = binding.address.text.toString()
        val user = UserModel(userName, nameOfRestaurant, email, password, address)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("accounts").child("admins").child(userId).setValue(user)
    }
}