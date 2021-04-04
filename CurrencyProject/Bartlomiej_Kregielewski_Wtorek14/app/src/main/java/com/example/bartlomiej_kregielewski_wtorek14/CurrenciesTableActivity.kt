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

    private fun loadCurrenciesData(response: JSONArray, table: ApiHelper.CurrenciesTables) {
        val ratesNew = response.getJSONObject(response.length() - 1).getJSONArray("rates")
        try {
            for (rateIndex in 0 until ratesNew.length()) {
                val name = ratesNew.getJSONObject(rateIndex).getString("currency")
                val code = ratesNew.getJSONObject(rateIndex).getString("code")
                val midNew = ratesNew.getJSONObject(rateIndex).getString("mid")
                val newCurrency = Currency(name, code, midNew.toDouble(), table)
                currenciesList.add(newCurrency)

                if (response.length() >= 2) {
                    val ratesOld = response.getJSONObject(response.length() - 2).getJSONArray("rates")
                    if (ratesOld.length() >= ratesNew.length()) {
                        val midOld = ratesOld.getJSONObject(rateIndex).getString("mid")
                        newCurrency.Rise = if (midNew == midOld) null else midNew > midOld
                    }
                }
            }
            listAdapter.notifyDataSetChanged()
        } catch(e: Exception) {
            ApiHelper.ToastError(e.message, this)
        }
    }


    private fun currenciesGetRequest(table: ApiHelper.CurrenciesTables) {
        val requestUrl = "${ApiHelper.baseUrl}/api/exchangerates/tables/$table/last/2/?${ApiHelper.formatJson}"
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