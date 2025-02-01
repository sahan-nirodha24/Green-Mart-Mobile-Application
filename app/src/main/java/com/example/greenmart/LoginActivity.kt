package com.example.greenmart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.greenmart.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Set up the login button click listener
        binding.loginButton.setOnClickListener {
            val loginUsername = binding.loginUsername.text.toString().trim()
            val loginPassword = binding.loginPassword.text.toString().trim()

            // Validate if the fields are empty
            if (loginUsername.isEmpty()) {
                binding.loginUsername.error = "Please enter your username"
                binding.loginUsername.requestFocus()
            } else if (loginPassword.isEmpty()) {
                binding.loginPassword.error = "Please enter your password"
                binding.loginPassword.requestFocus()
            } else {
                // If fields are filled, proceed to check credentials
                loginDatabase(loginUsername, loginPassword)
            }
        }

        // Set up the signup redirect button click listener
        binding.signupRedirect.setOnClickListener {
            // Navigate to the SignupActivity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()  // Finish the current activity to prevent going back to it
        }

        // Redirect to forgot password activity
        binding.ForgotRedirect.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun loginDatabase(username: String, password: String) {
        // Check if the user exists in the database
        val userExists = databaseHelper.readUser(username, password)

        // If user exists, show success message and navigate to MainActivity
        if (userExists) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Finish the LoginActivity to prevent going back to it
        } else {
            // If login failed, show failure message
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }
}
