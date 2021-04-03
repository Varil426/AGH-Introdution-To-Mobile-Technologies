package com.example.bartlomiej_kregielewski_wtorek14

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class GoldDetailsActivity : ApiActivity() {

    private lateinit var goldPriceTextView: TextView
    private lateinit var goldDetailsChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold_details)

        goldPriceTextView = findViewById(R.id.goldPriceTextView)
        goldDetailsChart = findViewById(R.id.goldDetailsChart)

        ApiHelper.InitializeChart(goldDetailsChart)

        GoldPriceRequest()
        GoldDataRequest(30)
    }

    private fun LoadGoldPrice(response: JSONArray) {
        val price = response.getJSONObject(0).getString("cena")
        goldPriceTextView.text = price
    }

    private fun GoldPriceRequest() {
        val requestUrl = "${ApiHelper.baseUrl}/api/cenyzlota/?${ApiHelper.formatJson}"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            requestUrl,
            null,
            { response -> LoadGoldPrice(response) },
            { error -> ApiHelper.ToastError(error.toString(), this) }
        )
        queue.add(jsonArrayRequest)
    }

    private fun LoadGoldData(response: JSONArray) {
        val entries = mutableListOf<Entry>()
        try {
            for (rateIndex in 0 until response.length()) {
                val currentObject = response.getJSONObject(rateIndex)
                val date = ApiHelper.DateStringToFloat(currentObject.getString("data"))
                val price = currentObject.getDouble("cena")

                entries.add(Entry(date, price.toFloat()))
            }
            val dataSet = LineDataSet(entries, getString(R.string.goldDetailsTitle))
            goldDetailsChart.data = LineData(dataSet)
            goldDetailsChart.invalidate()
        } catch(e: Exception) {
            ApiHelper.ToastError(e.message, this)
        }
    }

    private fun GoldDataRequest(days: Int) {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        val startDate = calendar.time
        val requestUrl = "${ApiHelper.baseUrl}/api/cenyzlota/${ApiHelper.dateFormat.format(startDate)}/${ApiHelper.dateFormat.format(endDate)}/?${ApiHelper.formatJson}"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            requestUrl,
            null,
            { response -> LoadGoldData(response) },
            { error -> ApiHelper.ToastError(error.toString(), this) }
        )
        queue.add(jsonArrayRequest)
    }
}