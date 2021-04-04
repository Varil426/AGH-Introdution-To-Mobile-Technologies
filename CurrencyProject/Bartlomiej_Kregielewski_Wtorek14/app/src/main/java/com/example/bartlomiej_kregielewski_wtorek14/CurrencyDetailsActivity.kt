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
import java.sql.Time
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

    private var loadedData = DataLoadedEnum.WEEK
    private val entries = mutableListOf<Entry>()

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

        val maxEnum = DataLoadedEnum.values().maxByOrNull { value -> value.days } ?: DataLoadedEnum.WEEK
        GetChartDataRequest(maxEnum)
    }

    private fun GetChartDataRequest(TimePeriod: DataLoadedEnum) {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, -TimePeriod.days)
        val startDate = calendar.time
        val requestUrl = "${ApiHelper.baseUrl}/api/exchangerates/rates/$table/$currencyCode/${ApiHelper.dateFormat.format(startDate)}/${ApiHelper.dateFormat.format(endDate)}/?${ApiHelper.formatJson}"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            requestUrl,
            null,
            { response -> LoadChartData(response, TimePeriod) },
            { error -> ApiHelper.ToastError(error.toString(), this) }
        )
        queue.add(jsonObjectRequest)
    }

    private fun LoadChartData(response: JSONObject, TimePeriod: DataLoadedEnum) {
        val rates = response.getJSONArray("rates")
        try {
            for (rateIndex in 0 until rates.length()) {
                val currentObject = rates.getJSONObject(rateIndex)
                val date = ApiHelper.DateStringToFloat(currentObject.getString("effectiveDate"))
                val mid = currentObject.getDouble("mid")

                entries.add(Entry(date, mid.toFloat()))
            }

            loadedData = TimePeriod

            if (entries.size >= 2) {
                val previousValue = entries.reversed()[1].y
                val currentValue = entries.last().y
                previousMidTextView.text = "${getString(R.string.previousMidText)} $previousValue"
                currentMidTextView.text = "${getString(R.string.currentMidText)} $currentValue"
            }

            ChangeChartData()
        } catch(e: Exception) {
            ApiHelper.ToastError(e.message, this)
        }
    }

    private fun UpdateChart(TimePeriod: DataLoadedEnum) {
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_YEAR, -TimePeriod.days)
        val dateAsFloat = ApiHelper.DateStringToFloat(ApiHelper.dateFormat.format(startDate.time))
        val dataSet = LineDataSet(entries.filter { entry -> entry.x >= dateAsFloat }, currencyCode)
        currencyDetailsChart.data = LineData(dataSet)
        currencyDetailsChart.invalidate()
    }

    private fun ChangeChartData() {
        switchCurrencyDataButton.text = getString(R.string.chartShowLastDaysButton).format(loadedData.days)

        when (loadedData) {
            DataLoadedEnum.WEEK -> {
                UpdateChart(DataLoadedEnum.MONTH)
                loadedData = DataLoadedEnum.MONTH
            }
            DataLoadedEnum.MONTH -> {
                UpdateChart(DataLoadedEnum.WEEK)
                loadedData = DataLoadedEnum.WEEK
            }
        }
    }
}