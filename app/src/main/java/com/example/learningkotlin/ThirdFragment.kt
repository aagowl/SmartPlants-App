package com.example.learningkotlin

//import android.R.layout
//line graph
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
        // on below line we are initializing
        // our variable with their ids.
        //lineGraphView = findViewById(R.id.idGraphView)
        // on below line we are adding data to our graph view.

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var Q1: Double
        var Q2: Double
        var Q3: Double
        var Q4: Double
        var plantQ1: Double
        var plantQ2: Double
        var plantQ3: Double

        //val sdf = SimpleDateFormat("E")
        //val calendar: Calendar = sdf.calendar
        //val date: Date = calendar.time
        var dateTime: Double

        //val dateTimeInLongTextView: TextView = findViewById(R.id.dateTimeLongValue)
        //val format1: TextView = binding.idTVHeadWellness

        val calendar: Calendar = Calendar.getInstance()
        var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("F")
        dateTime = simpleDateFormat.format(calendar.time).toDouble()
        binding.idTVHeadWellness.text = dateTime.toString()

        // on below line adding animation
        binding.idGraphViewWellness.animate()

        // on below line we are setting scrollable
        // for point graph view
        binding.idGraphViewWellness.viewport.isScrollable = true

        // on below line we are setting scalable.
        binding.idGraphViewWellness.viewport.isScalable = true

        // on below line we are setting scalable y
        binding.idGraphViewWellness.viewport.setScalableY(true)

        // on below line we are setting scrollable y
        binding.idGraphViewWellness.viewport.setScrollableY(true)

        binding.trackButton.setOnClickListener {
            Q1 = binding.seekBarOne.progress.toDouble()
            Q2 = binding.seekBarTwo.progress.toDouble()
            Q3 = binding.seekBarThree.progress.toDouble()
            Q4 = binding.seekBarFour.progress.toDouble()
            plantQ1 = binding.seekBarFive.progress.toDouble()
            plantQ2 = binding.seekBarSix.progress.toDouble()
            plantQ3 = binding.seekBarSeven.progress.toDouble()

            val series: LineGraphSeries<DataPoint> = LineGraphSeries(
                arrayOf(
                    // on below line we are adding
                    // each point on our x and y axis.
                    /*
                    DataPoint(dateTime, 1.0),
                    DataPoint(dateTime, 2.0),
                     */
                    DataPoint(1.0, Q1),
                    DataPoint(2.0, Q2),
                    DataPoint(3.0, Q3),
                    DataPoint(4.0, Q4),
                    DataPoint(5.0, plantQ1),
                    DataPoint(6.0, plantQ2),
                    DataPoint(7.0, plantQ3)
                )
            )

            // on below line we are setting color for series.
            series.color = Color.BLUE

            // on below line we are adding
            // data series to our graph view.

            binding.idGraphViewWellness.addSeries(series)
        }
    }

}