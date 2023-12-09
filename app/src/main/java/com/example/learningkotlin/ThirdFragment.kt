package com.example.learningkotlin

//import android.R.layout
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learningkotlin.databinding.FragmentThirdBinding
//line graph
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

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
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                // on below line we are adding
                // each point on our x and y axis.
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 3.0),
                DataPoint(2.0, 4.0),
                DataPoint(3.0, 9.0),
                DataPoint(4.0, 6.0),
                DataPoint(5.0, 3.0),
                DataPoint(6.0, 6.0),
                DataPoint(7.0, 1.0),
                DataPoint(8.0, 2.0)
            )
        )

        // on below line adding animation
        binding.idGraphView.animate()

        // on below line we are setting scrollable
        // for point graph view
        binding.idGraphView.viewport.isScrollable = true

        // on below line we are setting scalable.
        binding.idGraphView.viewport.isScalable = true

        // on below line we are setting scalable y
        binding.idGraphView.viewport.setScalableY(true)

        // on below line we are setting scrollable y
        binding.idGraphView.viewport.setScrollableY(true)

        // on below line we are setting color for series.
        series.color = Color.BLUE

        // on below line we are adding
        // data series to our graph view.
        binding.idGraphView.addSeries(series)
    }

}