package com.example.bartlomiej_kregielewski_wtorek14

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.bartlomiej_kregielewski_wtorek14.adapters.CurrencyListAdapter
import com.example.bartlomiej_kregielewski_wtorek14.entities.Currency
import org.json.JSONArray
import org.json.JSONObject

class CurrenciesTableActivity : ApiActivity() {

    private val currenciesList = mutableListOf<Currency>()

    private lateinit var currenciesRecyclerView: RecyclerView
    private lateinit var listAdapter: CurrencyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies_table)

        currenciesRecyclerView = findViewById(R.id.currenciesRecyclerView)

        currenciesGetRequest(ApiHelper.CurrenciesTables.A)
        currenciesGetRequest(ApiHelper.CurrenciesTables.B)

        listAdapter = CurrencyListAdapter(this, currenciesList)
        currenciesRecyclerView.adapter = listAdapter
        currenciesRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun riseSet(response: JSONObject, currency: Currency) {
        val rates = response.getJSONArray("rates")
        if (rates.length() >= 2) {
            try {
                val yesterdaysValue = rates.getJSONObject(0).getDouble("mid")
                val todaysValue = rates.getJSONObject(1).getDouble("mid")
                currency.Rise = yesterdaysValue < todaysValue
                listAdapter.notifyItemChanged(currenciesList.indexOf(currency))
            } catch(e: Exception) {
                ApiHelper.ToastError(e.message, this)
            }
        }
    }

    private fun riseRequest(currency: Currency) {
        val topCount = 2
        val requestUrl = "${ApiHelper.baseUrl}/api/exchangerates/rates/${currency.Table}/${currency.CurrencyCode}/last/$topCount/?${ApiHelper.formatJson}"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            requestUrl,
            null,
            { response -> riseSet(response, currency) },
            { error -> ApiHelper.ToastError(error.toString(), this) }
        )
        queue.add(jsonObjectRequest)
    }

    private fun loadCurrenciesData(response: JSONArray, table: ApiHelper.CurrenciesTables) {
        val rates = response.getJSONObject(0).getJSONArray("rates")
        try {
            for (rateIndex in 0 until rates.length()) {
                val name = rates.getJSONObject(rateIndex).getString("currency")
                val code = rates.getJSONObject(rateIndex).getString("code")
                val mid = rates.getJSONObject(rateIndex).getString("mid")
                currenciesList.add(Currency(name, code, mid.toDouble(), table))
            }
            listAdapter.notifyDataSetChanged()
        } catch(e: Exception) {
            ApiHelper.ToastError(e.message, this)
        }
        for (currency in currenciesList) {
            riseRequest(currency)
        }
    }


    private fun currenciesGetRequest(table: ApiHelper.CurrenciesTables) {
        val requestUrl = "${ApiHelper.baseUrl}/api/exchangerates/tables/$table/?${ApiHelper.formatJson}"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            requestUrl,
            null,
            { response -> loadCurrenciesData(response, table) },
            { error -> ApiHelper.ToastError(error.toString(), this) }
        )
        queue.add(jsonArrayRequest)
    }
}