package com.example.bartlomiej_kregielewski_wtorek14

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.bartlomiej_kregielewski_wtorek14.entities.Currency
import com.github.mikephil.charting.charts.LineChart
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoField

class ApiHelper private constructor(){
    companion object {
        val baseUrl = "https://api.nbp.pl"
        val formatJson = "format=json"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        fun ToastError(errorMessage: String?, context: Context) {
            Toast.makeText(context, errorMessage ?: "ERROR", Toast.LENGTH_LONG).show()
        }

        fun InitializeChart(chart: LineChart) {
            chart.isDragEnabled = true
            chart.setScaleEnabled(false)
            chart.description.isEnabled = false
            chart.xAxis.valueFormatter = DateValueFormatter()
        }

        fun DateStringToFloat(date: String): Float {
            return LocalDate.parse(date).getLong(ChronoField.EPOCH_DAY).toFloat() * 86400 * 1000
        }
    }

    enum class CurrenciesTables {
        A,
        B
    }
}