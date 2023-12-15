package com.example.learningkotlin

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.learningkotlin.databinding.FragmentThirdBinding
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
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

    private fun loadData(){
        //val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        //val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key)
        //val highScore = sharedPref.getInt("Biden", defaultValue)

        /*
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                // on below line we are adding
                // each point on our x and y axis.
                DataPoint(1.0, 1.0),
                DataPoint(2.0, 2.0),
                DataPoint(3.0, 3.0),
                DataPoint(4.0, 4.0),
                DataPoint(5.0, 5.0),
                DataPoint(6.0, 6.0),
                DataPoint(7.0, 7.0),
            )
        )

        // on below line we are setting color for series.
        series.color = Color.BLUE
        // on below line we are adding
        // data series to our graph view.
        binding.idGraphViewWellness.addSeries(series)

         */
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateTime: String
        val calendar: Calendar = Calendar.getInstance()
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("u")
        dateTime = simpleDateFormat.format(calendar.time)
        binding.idTVHeadWellness.text = dateTime

        // on below line adding animation
        binding.idGraphViewWellness.animate()

        // on below line we are setting scrollable
        // for point graph view
        //binding.idGraphViewWellness.viewport.isScrollable = false
        // on below line we are setting scalable.
        binding.idGraphViewWellness.viewport.isScalable = true

        // on below line we are setting scalable y
        binding.idGraphViewWellness.viewport.setScalableY(true)

        // on below line we are setting scrollable y
        //binding.idGraphViewWellness.viewport.setScrollableY(false)

        binding.idGraphViewWellness.viewport.setMaxX(12.0)
        binding.idGraphViewWellness.viewport.setMinX(0.0)

        binding.idGraphViewWellness.viewport.setMaxY(10.0)
        binding.idGraphViewWellness.viewport.setMinY(0.0)

        binding.idGraphViewWellness.viewport.isYAxisBoundsManual = true
        binding.idGraphViewWellness.viewport.isXAxisBoundsManual = true

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                // on below line we are adding
                // each point on our x and y axis.
                DataPoint(0.0, 1.0),
                DataPoint(2.0, 2.0),
                DataPoint(4.0, 7.0),
                DataPoint(6.0, 10.0),
                DataPoint(8.0, 3.0),
                DataPoint(10.0, 4.0),
                DataPoint(12.0, 6.0),
            )
        )

        if (binding.idGraphViewWellness.viewport.isYAxisBoundsManual)
            series.color = Color.BLUE
        else
            series.color = Color.RED



        binding.idGraphViewWellness.addSeries(series)

        binding.trackButton.setOnClickListener {
            //saveData()
            //loadData()
        }
    }

}