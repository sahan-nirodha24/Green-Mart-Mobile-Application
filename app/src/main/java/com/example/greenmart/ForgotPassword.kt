package com.example.greenmart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.greenmart.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Set onClickListener for the reset password button
        binding.resetPasswordButton.setOnClickListener {

            val username = binding.forgotUsername.text.toString().trim()
            val email = binding.forgotEmail.text.toString().trim()
            val phone = binding.forgotPhone.text.toString().trim()
            val newPassword = binding.forgotPassword.text.toString().trim()

            // Validate input fields
            var isValid = true

            // Check for empty fields
            if (username.isEmpty()) {
                binding.forgotUsername.error = "Please enter your username"
                isValid = false
            }
            if (email.isEmpty()) {
                binding.forgotEmail.error = "Please enter your email"
                isValid = false
            }
            if (phone.isEmpty()) {
                binding.forgotPhone.error = "Please enter your phone number"
                isValid = false
            }
            if (newPassword.isEmpty()) {
                binding.forgotPassword.error = "Please enter your new password"
                isValid = false
            }

            // If any field is empty, return
            if (!isValid) {
                return@setOnClickListener
            }

            // Check if user exists
            val userDetails = databaseHelper.getUserDetails(username)
            if (userDetails != null) {
                if (userDetails["email"] == email && userDetails["phone"] == phone) {
                    // Attempt to update the password
                    val updateResult = databaseHelper.updatePassword(username, newPassword)
                    if (updateResult == 1) {
                        Toast.makeText(this, "Password reset successfully", Toast.LENGTH_SHORT).show()

                        // Clear all input fields after successful reset
                        clearInputFields()
                    } else if (updateResult == -1) {
                        Toast.makeText(this, "New password cannot be the same as the current password", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to reset password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Incorrect email or phone number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }

        // Redirect to login activity
        binding.loginRedirect.setOnClickListener {
            // No need to finish the activity; user can still reset the password
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Helper method to clear input fields
    private fun clearInputFields() {
        binding.forgotUsername.text.clear()
        binding.forgotEmail.text.clear()
        binding.forgotPhone.text.clear()
        binding.forgotPassword.text.clear()
    }
}

