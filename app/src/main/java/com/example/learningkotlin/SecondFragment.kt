package com.example.learningkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.learningkotlin.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
  private val exp = 0
  private val level = 1
  private val levelgrow = 5
  //makes variables for the leveling system
  private var _binding: FragmentSecondBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      _binding = FragmentSecondBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFindPlant.setOnClickListener {
            print("works??")
            /*
            exp = exp+5
            */
        }
    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
/*
if(exp == levelgrow*level){
  level++
}
//level up, exp required to level up increases by 5 everytime they level up

 */
}