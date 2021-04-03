package com.example.bartlomiej_kregielewski_wtorek14

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.util.*

class CurrencyDetailsActivity : ApiActivity() {

    private lateinit var currencyCode: String
    private lateinit var table: String

    private lateinit var currencyCodeTextView: TextView
    private lateinit var previousMidTextView: TextView
    private lateinit var currentMidTextView: TextView
    private lateinit var currencyDetailsChart: LineChart
    private lateinit var switchCurrencyDataButton: Button

    private var loadedData = DataLoadedEnum.MONTH

    private enum class DataLoadedEnum(val days: Int) {
        WEEK(7),
        MONTH(30)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_details)

        currencyCodeTextView = findViewById(R.id.currencyCodeTextView)
        previousMidTextView = findViewById(R.id.previousMidTextView)
        currentMidTextView = findViewById(R.id.currentMidTextView)
        currencyDetailsChart = findViewById(R.id.currencyChart)
        switchCurrencyDataButton = findViewById(R.id.switchCurrencyDataButton)

        currencyCode = intent.getStringExtra("Currency Code") ?: throw Exception("Missing Currency Code.")
        table = intent.getStringExtra("Table") ?: throw Exception("Missing Table.")

        currencyCodeTextView.text = currencyCode
        ApiHelper.InitializeChart(currencyDetailsChart)
        switchCurrencyDataButton.setOnClickListener { ChangeChartData() }

        CurrencyLastValuesRequest(2)
        ChangeChartData()
    }

    private fun ChangeChartData() {
        when (loadedData) {
            DataLoadedEnum.WEEK -> {
                CurrencyDataRequest(DataLoadedEnum.MONTH.days)
                loadedData = DataLoadedEnum.MONTH
                switchCurrencyDataButton.text = getString(R.string.chartShowWeek)
            }
            DataLoadedEnum.MONTH -> {
                CurrencyDataRequest(DataLoadedEnum.WEEK.days)
                loadedData = DataLoadedEnum.WEEK
                switchCurrencyDataButton.text = getString(R.string.chartShowMonth)
            }
        }
    }

    private fun LoadLastTwoValues(response: JSONObject) {
        val rates = response.getJSONArray("rates")
        if (rates.length() >= 2) {
            try {
                val previousValue = rates.getJSONObject(0).getString("mid")
                val currentValue = rates.getJSONObject(1).getString("mid")
                previousMidTextView.text = "${getString(R.string.previousMidText)} $previousValue"
                currentMidTextView.text = "${getString(R.string.currentMidText)} $currentValue"
            } catch(e: Exception) {
                ApiHelper.ToastError(e.message, this)
            }
        }
    }

    private fun CurrencyLastValuesRequest(last: Int) {
        val requestUrl = "${ApiHelper.baseUrl}/api/exchangerates/rates/$table/$currencyCode/last/$last/?${ApiHelper.formatJson}"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            requestUrl,
            null,
            { response -> LoadLastTwoValues(response) },
            { error -> ApiHelper.ToastError(error.toString(), this) }
        )
        queue.add(jsonObjectRequest)
    }

    private fun LoadCurrencyData(response: JSONObject) {
        val rates = response.getJSONArray("rates")
        val entries = mutableListOf<Entry>()
        try {
            for (rateIndex in 0 until rates.length()) {
                val currentObject = rates.getJSONObject(rateIndex)
                val date = ApiHelper.DateStringToFloat(currentObject.getString("effectiveDate"))
                val mid = currentObject.getDouble("mid")

                entries.add(Entry(date, mid.toFloat()))
            }
            val dataSet = LineDataSet(entries, currencyCode)
            currencyDetailsChart.data = LineData(dataSet)
            currencyDetailsChart.invalidate()
        } catch(e: Exception) {
            ApiHelper.ToastError(e.message, this)
        }
    }

    private fun CurrencyDataRequest(days: Int) {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        val startDate = calendar.time
        val requestUrl = "${ApiHelper.baseUrl}/api/exchangerates/rates/$table/$currencyCode/${ApiHelper.dateFormat.format(startDate)}/${ApiHelper.dateFormat.format(endDate)}/?${ApiHelper.formatJson}"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            requestUrl,
            null,
            { response -> LoadCurrencyData(response) },
            { error -> ApiHelper.ToastError(error.toString(), this) }
        )
        queue.add(jsonObjectRequest)
    }
}