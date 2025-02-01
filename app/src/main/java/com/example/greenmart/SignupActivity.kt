package com.example.greenmart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.greenmart.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Signup button click listener
        binding.signupButton.setOnClickListener {
            val signupUsername = binding.signupUsername.text.toString().trim()
            val signupPassword = binding.signupPassword.text.toString().trim()
            val signupEmail = binding.signupEmail.text.toString().trim()
            val signupPhone = binding.signupPhone.text.toString().trim()
            val signupLocation = binding.signupLocation.text.toString().trim()

            // Validate if any fields are empty
            if (signupUsername.isEmpty()) {
                binding.signupUsername.error = "Please enter your username"
                binding.signupUsername.requestFocus()
            } else if (signupEmail.isEmpty()) {
                binding.signupEmail.error = "Please enter your email"
                binding.signupEmail.requestFocus()
            } else if (signupPhone.isEmpty()) {
                binding.signupPhone.error = "Please enter your phone number"
                binding.signupPhone.requestFocus()
            } else if (signupLocation.isEmpty()) {
                binding.signupLocation.error = "Please enter your location"
                binding.signupLocation.requestFocus()
            } else if (signupPassword.isEmpty()) {
                binding.signupPassword.error = "Please enter your password"
                binding.signupPassword.requestFocus()
            } else {
                // If all fields are filled, proceed to signup
                signupDatabase(signupUsername, signupPassword, signupEmail, signupPhone, signupLocation)
            }
        }

        // Redirect to login activity
        binding.loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back to it
        }
    }

    private fun signupDatabase(
        username: String,
        password: String,
        email: String,
        phone: String,
        location: String
    ) {
        // Insert user into the database and check if the username, email, or phone already exists
        val insertedRowId = databaseHelper.insertUser(username, password, email, phone, location)

        if (insertedRowId == -1L) {
            // Show a message indicating the user already exists
            Toast.makeText(this, "Username, email, or phone number already exists", Toast.LENGTH_SHORT).show()
        } else {
            // If insertion is successful
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finish the SignupActivity to prevent going back to it
        }
    }
}
