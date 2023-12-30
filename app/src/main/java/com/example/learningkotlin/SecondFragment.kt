package com.example.learningkotlin

import android.annotation.SuppressLint
import android.content.Context
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

  private var _binding: FragmentSecondBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = FragmentSecondBinding.inflate(inflater, container, false)
      return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFindPlant.setOnClickListener {
            val levelingPref = activity?.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

            val gainedXP = 25
            var xp: Int = levelingPref!!.getInt((R.string.xp_key).toString(), -1)
            var level: Int = levelingPref.getInt((R.string.level_key).toString(), -1)

            if (xp != -1 && level != -1)
            {
                xp+=gainedXP
                if (xp >= 100)
                {
                    level++
                    xp -= 100
                    binding.textNextLevel.text = "Leveled Up! New Level:"
                    binding.textLevelInfo.text = level.toString()
                }
                else
                {
                    binding.textNextLevel.text = "XP until next level:"
                    binding.textLevelInfo.text = (100-xp).toString()
                }
                with (levelingPref.edit()) {
                    putInt((R.string.xp_key).toString(), xp)
                    putInt((R.string.level_key).toString(), level)
                    commit()
                }
            }
            binding.textXPGained.text = gainedXP.toString()
        }
    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}