package com.example.learningkotlin

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.learningkotlin.databinding.FragmentThirdBinding
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    // This property is only valid between onCreateView and onDestroyView.

    private val binding get() = _binding!!

    lateinit var sharedPreferences: SharedPreferences
    private var Q1: Double? = null
    private var Q2: Double? = null
    private var Q3: Double? = null
    private var Q4: Double? = null
    private var plantQ1: Double? = null
    private var plantQ2: Double? = null
    private var plantQ3: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveData(){
        Q1 = binding.seekBarOne.progress.toDouble()
        Q2 = binding.seekBarTwo.progress.toDouble()
        Q3 = binding.seekBarThree.progress.toDouble()
        Q4 = binding.seekBarFour.progress.toDouble()
        plantQ1 = binding.seekBarFive.progress.toDouble()
        plantQ2 = binding.seekBarSix.progress.toDouble()
        plantQ3 = binding.seekBarSeven.progress.toDouble()

        /*val sharedPreferences = getSharedPreferences("Shared prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(
            "QuestionOneValue", Q1!!.toInt()).apply()

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("Biden", Q1!!.toInt())
            apply()
        }
         */
    }

    private fun loadData(): LineGraphSeries<DataPoint> {
        //val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        //val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key)
        //val highScore = sharedPref.getInt("Biden", defaultValue)

        return LineGraphSeries(
            arrayOf(
                // on below line we are adding
                // each point on our x and y axis.
                DataPoint(1.0, Q1!!),
                DataPoint(2.0, Q2!!),
                DataPoint(3.0, Q3!!),
                DataPoint(4.0, Q4!!),
                DataPoint(5.0, plantQ1!!),
                DataPoint(6.0, plantQ2!!),
                DataPoint(7.0, plantQ3!!),
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dateTime: Double
        val calendar: Calendar = Calendar.getInstance()
        var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("F")
        dateTime = simpleDateFormat.format(calendar.time).toDouble()
        binding.idTVHeadWellness.text = dateTime.toString()

        // on below line adding animation
        binding.idGraphViewWellness.animate()

        // on below line we are setting scrollable for point graph view
        binding.idGraphViewWellness.viewport.isScrollable = true

        // on below line we are setting scalable.
        binding.idGraphViewWellness.viewport.isScalable = true

        // on below line we are setting scalable y
        binding.idGraphViewWellness.viewport.setScalableY(true)

        // on below line we are setting scrollable y
        binding.idGraphViewWellness.viewport.setScrollableY(true)

        binding.trackButton.setOnClickListener {

            saveData()
            val series: LineGraphSeries<DataPoint> = loadData()

            // on below line we are setting color for series.
            series.color = Color.BLUE

            // on below line we are adding data series to our graph view.
            binding.idGraphViewWellness.addSeries(series)
        }
    }

}