package com.example.greenmart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.*
import java.lang.Exception

data class Message(val content: String, val isUser: Boolean)

class ChatBot : AppCompatActivity() {
    private val messages = mutableListOf<Message>()
    private lateinit var adapter: MessageAdapter
    private var lastApiCallTime: Long = 0
    private val apiCooldown = 2000L // 2 seconds cooldown between requests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        val eTPrompt = findViewById<EditText>(R.id.eTPrompt)
        val btnSubmit = findViewById<ImageButton>(R.id.btnSubmit)
        val rvMessages = findViewById<RecyclerView>(R.id.rvMessages)
        val btnReturnHome = findViewById<Button>(R.id.btnReturnHome)

        // Set up RecyclerView
        adapter = MessageAdapter(messages)
        rvMessages.adapter = adapter
        rvMessages.layoutManager = LinearLayoutManager(this)

        btnSubmit.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastApiCallTime < apiCooldown) {
                Toast.makeText(this, "Please wait before sending another message.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lastApiCallTime = currentTime

            val prompt = eTPrompt.text.toString()
            if (prompt.isNotBlank()) {
                // Add user message
                messages.add(Message(prompt, isUser = true))
                adapter.notifyItemInserted(messages.size - 1)
                rvMessages.scrollToPosition(messages.size - 1)

                // Clear input field
                eTPrompt.text.clear()

                // Generate bot response
                val context = buildConversationContext()
                fetchBotResponse(context)
            }
        }

        // Set up returnHome click listener
        btnReturnHome.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    /**
     * Builds the conversation context by combining all previous messages into a single string.
     */
    private fun buildConversationContext(): String {
        val contextBuilder = StringBuilder()
        for (message in messages) {
            if (message.isUser) {
                contextBuilder.append("User: ${message.content}\n")
            } else {
                contextBuilder.append("Bot: ${message.content}\n")
            }
        }
        return contextBuilder.toString()
    }

    /**
     * Fetches the bot's response using the Generative AI model.
     */
    private fun fetchBotResponse(context: String) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-pro-latest", // Replace with actual model name
            apiKey = "AIzaSyDE6y3-7cSlSus9hqy_k1paQiNo4rPqJS4" // Replace with actual API key
        )

        CoroutineScope(Dispatchers.IO).launch {
            var retryCount = 0
            val maxRetries = 3
            val delayDuration = 1000L // Start with 1 second

            while (retryCount < maxRetries) {
                try {
                    val response = generativeModel.generateContent(context)
                    withContext(Dispatchers.Main) {
                        // Add bot message
                        messages.add(Message(response.text.toString(), isUser = false))
                        adapter.notifyItemInserted(messages.size - 1)
                        findViewById<RecyclerView>(R.id.rvMessages).scrollToPosition(messages.size - 1)
                    }
                    return@launch
                } catch (e: Exception) {
                    retryCount++
                    if (retryCount == maxRetries || e.message?.contains("RESOURCE_EXHAUSTED") == true) {
                        withContext(Dispatchers.Main) {
                            messages.add(Message("Quota exceeded or API limit reached. Please try again later.", isUser = false))
                            adapter.notifyItemInserted(messages.size - 1)
                            findViewById<RecyclerView>(R.id.rvMessages).scrollToPosition(messages.size - 1)
                        }
                        return@launch
                    }
                    delay(delayDuration * retryCount) // Exponential backoff
                }
            }
        }
    }
}
