package com.example.greenmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_menu, container, false)

        val cat01 = rootView.findViewById<ImageView>(R.id.cat01)
        val cat02 = rootView.findViewById<ImageView>(R.id.cat02)
        val cat03 = rootView.findViewById<ImageView>(R.id.cat03)
        val cat04 = rootView.findViewById<ImageView>(R.id.cat04)
        val cat05 = rootView.findViewById<ImageView>(R.id.cat05)
        val cat06 = rootView.findViewById<ImageView>(R.id.cat06)
        val cat07 = rootView.findViewById<ImageView>(R.id.cat07)
        val cat08 = rootView.findViewById<ImageView>(R.id.cat08)

        cat01.setOnClickListener{
            openFragment(BakeryFragment())
        }

        cat02.setOnClickListener{
            openFragment(BiscuitFragment())
        }

        cat03.setOnClickListener{
            openFragment(DiaryFragment())
        }

        cat04.setOnClickListener{
            openFragment(FruitFragment())
        }

        cat05.setOnClickListener{
            openFragment(VegetableFragment())
        }

        cat06.setOnClickListener{
            openFragment(CareFragment())
        }

        cat07.setOnClickListener{
            openFragment(BeverageFragment())
        }

        cat08.setOnClickListener{
            openFragment(PharmacyFragment())
        }

        return rootView

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)  // Replace with your container ID
        transaction.addToBackStack(null)  // Optional, to add to back stack
        transaction.commit()
    }


}