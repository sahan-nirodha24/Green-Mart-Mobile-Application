package com.example.greenmart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greenmart.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using ViewBinding
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listeners for each card
        binding.item1.setOnClickListener {
            startActivity(Intent(activity, TextOcr::class.java))
        }

        binding.item2.setOnClickListener {
            startActivity(Intent(activity, Translator::class.java))
        }

        binding.item3.setOnClickListener {
            startActivity(Intent(activity, ChatBot::class.java))
        }
    }
}
