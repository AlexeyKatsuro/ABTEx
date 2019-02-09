package com.e.btex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.e.btex.databinding.FragmentMainBinding

class MainFragment : Fragment(){

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }
}