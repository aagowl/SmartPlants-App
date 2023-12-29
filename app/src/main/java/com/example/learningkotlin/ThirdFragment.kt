package com.example.learningkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.learningkotlin.databinding.FragmentThirdBinding
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var dateTime: Double? = null
    private val calendar: Calendar = Calendar.getInstance()
    @SuppressLint("SimpleDateFormat")
    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("u")

    private var sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveData(){
        val Q1 = binding.seekBarOne.progress.toDouble()
        val Q2 = binding.seekBarTwo.progress.toDouble()
        val Q3 = binding.seekBarThree.progress.toDouble()
        val Q4 = binding.seekBarFour.progress.toDouble()
        val plantQ1 = binding.seekBarFive.progress.toDouble()
        val plantQ2 = binding.seekBarSix.progress.toDouble()
        val plantQ3 = binding.seekBarSeven.progress.toDouble()

        dateTime = simpleDateFormat.format(calendar.time).toDouble()
        val key: Int = dateTime!!.toInt()-1     //Format: IntInt   Question#Day


        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref!!.edit()) {
            putInt("0${key}", Q1.toInt())
            putInt("1${key}", Q2.toInt())
            putInt("2${key}", Q3.toInt())
            putInt("3${key}", Q4.toInt())
            putInt("4${key}", plantQ1.toInt())
            putInt("5${key}", plantQ2.toInt())
            putInt("6${key}", plantQ3.toInt())
            commit()
        }
    }

    private fun graphData(series: LineGraphSeries<DataPoint>, question: Int){
        if (question == 0)
        {
            series.color = Color.rgb(255,92,92)
            binding.idGraphViewWellness.addSeries(series)
        }
        else if (question == 1)
        {
            series.color = Color.rgb(57,255,20)
            binding.idGraphViewWellness.addSeries(series)
        }
        else if (question == 2)
        {
            series.color = Color.rgb(117,230,218)
            binding.idGraphViewWellness.addSeries(series)
        }
        else if (question == 3)
        {
            series.color = Color.rgb(191,64,191)
            binding.idGraphViewWellness.addSeries(series)
        }
        else if (question == 4)
        {
            series.color = Color.rgb(255,203,71)
            binding.idGraphViewPlant.addSeries(series)
        }
        else if (question == 5)
        {
            series.color = Color.rgb(117,118,189)
            binding.idGraphViewPlant.addSeries(series)
        }
        else if (question == 6)
        {
            series.color = Color.rgb(89,139,44)
            binding.idGraphViewPlant.addSeries(series)
        }
    }

    private fun graphData(series: PointsGraphSeries<DataPoint>, question: Int, size: Float){
        if (question == 0)
        {
            series.color = Color.rgb(255,92,92)
            binding.idGraphViewWellness.addSeries(series)
        }
        else if (question == 1)
        {
            series.color = Color.rgb(57,255,20)
            binding.idGraphViewWellness.addSeries(series)
        }
        else if (question == 2)
        {
            series.color = Color.rgb(117,230,218)
            binding.idGraphViewWellness.addSeries(series)
        }
        else if (question == 3)
        {
            series.color = Color.rgb(191,64,191)
            binding.idGraphViewWellness.addSeries(series)
        }
        else if (question == 4)
        {
            series.color = Color.rgb(255,203,71)
            binding.idGraphViewPlant.addSeries(series)
        }
        else if (question == 5)
        {
            series.color = Color.rgb(117,118,189)
            binding.idGraphViewPlant.addSeries(series)
        }
        else if (question == 6)
        {
            series.color = Color.rgb(89,139,44)
            binding.idGraphViewPlant.addSeries(series)
        }
        series.shape = PointsGraphSeries.Shape.POINT
        series.size = size
    }

    private fun loadData(){
        binding.idGraphViewWellness.removeAllSeries()
        binding.idGraphViewPlant.removeAllSeries()

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        var lineKey: String
        var pointKey: String
        var pointSize: Float
        var lineData: Double
        var pointData: Double
        var pointWellnessOne: Double? = null
        var pointWellnessTwo: Double? = null
        var pointWellnessThree: Double? = null
        var pointPlantOne: Double? = null
        var pointPlantTwo: Double? = null


        //Where i is the question number, and e is the number of the day in the week
        for (i in 0..6)
        {
            val seriesLine: LineGraphSeries<DataPoint> = LineGraphSeries(
                emptyArray()
            )
            for (e in 0..6)
            {
                pointSize = 20f
                val singlePointSeries: PointsGraphSeries<DataPoint> = PointsGraphSeries(
                    emptyArray()
                )
                //key Format: IntInt   Question#Day   ie
                lineKey = i.toString()
                lineKey += e.toString()

                pointKey = e.toString()
                pointKey += i.toString()

                lineData = sharedPref!!.getInt(lineKey, -1).toDouble()
                pointData = sharedPref!!.getInt(pointKey, -1).toDouble()

                if(pointData != -1.0)
                {
                    if (e == 0){
                        pointWellnessOne = pointData
                    }
                    else if (e == 1){
                        pointWellnessTwo = pointData
                        if (pointWellnessTwo == pointWellnessOne){
                            pointSize-=5f
                        }
                    }
                    else if (e == 2){
                        pointWellnessThree = pointData
                        if (pointWellnessThree == pointWellnessOne){
                            pointSize-=5f
                        }
                        if (pointWellnessThree == pointWellnessTwo){
                            pointSize-=5f
                        }
                    }
                    else if (e == 3){
                        if (pointData == pointWellnessOne){
                            pointSize-=5f
                        }
                        if (pointData == pointWellnessTwo){
                            pointSize-=5f
                        }
                        if (pointData == pointWellnessThree){
                            pointSize-=5f
                        }
                    }
                    else if (e == 4){
                        pointPlantOne = pointData
                    }
                    else if (e == 5){
                        pointPlantTwo = pointData
                        if (pointPlantTwo == pointPlantOne){
                            pointSize-=7f
                        }
                    }
                    else {
                        if(pointData == pointPlantOne){
                            pointSize-=7f
                        }
                        if(pointData == pointPlantTwo){
                            pointSize-=7f
                        }
                    }
                    singlePointSeries.appendData(DataPoint((i * 2).toDouble(), pointData), true, 1)
                    graphData(singlePointSeries, e, pointSize)
                }
                if(lineData != -1.0)
                {
                    seriesLine.appendData(DataPoint((e * 2).toDouble(), lineData), true, 7)
                }
            }
            graphData(seriesLine, i)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        // on below line adding animation
        binding.idGraphViewWellness.animate()

        binding.idGraphViewPlant.animate()

        // on below line we are setting scalable.
        binding.idGraphViewWellness.viewport.isScalable = true

        binding.idGraphViewPlant.viewport.isScalable = true

        // on below line we are setting scalable y
        binding.idGraphViewWellness.viewport.setScalableY(true)

        binding.idGraphViewPlant.viewport.setScalableY(true)

        binding.idGraphViewWellness.viewport.setMaxX(12.0)
        binding.idGraphViewWellness.viewport.setMinX(0.0)
        binding.idGraphViewWellness.viewport.setMaxY(10.0)
        binding.idGraphViewWellness.viewport.setMinY(0.0)

        binding.idGraphViewPlant.viewport.setMaxX(12.0)
        binding.idGraphViewPlant.viewport.setMinX(0.0)
        binding.idGraphViewPlant.viewport.setMaxY(10.0)
        binding.idGraphViewPlant.viewport.setMinY(0.0)

        binding.idGraphViewWellness.viewport.isYAxisBoundsManual = true
        binding.idGraphViewWellness.viewport.isXAxisBoundsManual = true

        binding.idGraphViewPlant.viewport.isYAxisBoundsManual = true
        binding.idGraphViewPlant.viewport.isXAxisBoundsManual = true

        binding.trackButton.setOnClickListener {
            val levelingPref = activity?.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            val gainedXp = 25
            var xp: Int = levelingPref!!.getInt((R.string.xp_key).toString(), -1)
            var level: Int = levelingPref.getInt((R.string.level_key).toString(), -1)

            println(xp)
            println(level)
            if (xp != -1 && level != -1)
            {
                xp+=gainedXp
                if (xp >= 100)
                {
                    level++
                    xp -= 100
                }
                with (levelingPref.edit()) {
                    putInt((R.string.xp_key).toString(), xp)
                    putInt((R.string.level_key).toString(), level)
                    commit()
                }
            }


            saveData()
            loadData()
        }
    }
}