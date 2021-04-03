package com.example.bartlomiej_kregielewski_wtorek14

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*

class DateValueFormatter : ValueFormatter() {
    /*override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val date = Date(value.toLong())
        return ApiHelper.dateFormat.format(date)
    }*/

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val date = Date(value.toLong())
        return ApiHelper.dateFormat.format(date)
    }

    /*override fun getPointLabel(entry: Entry?): String {
        return if (entry != null) "${ApiHelper.dateFormat.format(Date(entry.x.toLong()))}: ${entry.y}" else ""
    }*/
}