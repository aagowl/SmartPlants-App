package com.example.learningkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learningkotlin.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      _binding = FragmentFirstBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView.setImageResource(R.drawable.plant_cropped)

        val levelingPref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        var xp: Int = levelingPref!!.getInt((R.string.xp_key).toString(), -1)
        var level: Int = levelingPref.getInt((R.string.level_key).toString(), -1)
        if (level == -1)
        {
            level = 1
            xp = 5
            with (levelingPref.edit()) {
                putInt((R.string.xp_key).toString(), xp)
                putInt((R.string.level_key).toString(), level)
                commit()
            }
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonSecond.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)

            // type of the content to be shared
            sharingIntent.type = "text/plain"

            // Body of the content
            val shareBody = "Share your plant!"

            // subject of the content. you can share anything
            val shareSubject = "Your Subject Here"

            // passing body of the content
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

            // passing subject of the content
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }

        binding.buttonThird.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_thirdFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        val levelingPref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val xp: Int = levelingPref!!.getInt((R.string.xp_key).toString(), -1)
        var level: Int = levelingPref.getInt((R.string.level_key).toString(), -1)
        binding.progressBarLevel.setProgress(xp, true)
        binding.textLevel.text = level.toString()
        binding.textXP.text = xp.toString()
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}