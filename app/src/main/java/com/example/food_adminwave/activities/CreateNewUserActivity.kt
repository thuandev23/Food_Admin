package com.example.food_adminwave.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.example.food_adminwave.databinding.ActivityCreateNewUserBinding
import com.example.food_adminwave.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class CreateNewUserActivity : AppCompatActivity() {
    //firebase
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding: ActivityCreateNewUserBinding by lazy {
        ActivityCreateNewUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // initialize Firebase Auth
        auth = Firebase.auth
        // initialize Firebase Database
        database = Firebase.database.reference

        binding.imgBtnBack.setOnClickListener {
            finish()
        }
        binding.btnCreateNewUser.setOnClickListener {
            // get text form edittext
            userName = binding.name.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()
            if (userName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                createNewAccount(email, password)
            }
        }
        var isPasswordVisible = false
        binding.showPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(isPasswordVisible)
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
                finish()
            } else {
                Toast.makeText(this, "Account created failed: ${task.exception}", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createNewAccount: Failed", task.exception)
            }
        }
    }
    private fun saveUserData() {
        userName = binding.name.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = UserModel(userName, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("accounts").child("users").child(userId).setValue(user)
    }
}