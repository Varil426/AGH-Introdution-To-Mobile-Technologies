package com.example.bartlomiej_kregielewski_wtorek14

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.bartlomiej_kregielewski_wtorek14.entities.Currency
import org.json.JSONArray

class ExchangeRateCalculatorActivity : ApiActivity() {

    private val currenciesList = mutableListOf<Currency>()

    private lateinit var plnEditTextNumber: EditText
    private lateinit var otherCurrencyEditTextNumber: EditText

    private lateinit var otherCurrencySpinner: Spinner

    private lateinit var convertToPlnButton: Button
    private lateinit var convertToOtherCurrencyButton: Button

    private var selectedCurrency: Currency? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rate_calculator)

        plnEditTextNumber = findViewById(R.id.plnEditTextNumber)
        otherCurrencyEditTextNumber = findViewById(R.id.otherCurrencyEditTextNumber)

        otherCurrencySpinner = findViewById(R.id.otherCurrencySpinner)
        otherCurrencySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCurrency = currenciesList[position]
                convertToPlnButton.text = getString(R.string.convert_other_to_pln).format(selectedCurrency?.CurrencyCode ?: "X")
                convertToOtherCurrencyButton.text = getString(R.string.conver_pln_to_other).format(selectedCurrency?.CurrencyCode ?: "X")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                convertToPlnButton.text = getString(R.string.convert_other_to_pln).format("X")
                convertToOtherCurrencyButton.text = getString(R.string.conver_pln_to_other).format("X")
            }

        }

        convertToPlnButton = findViewById(R.id.convertPlnButton)
        convertToOtherCurrencyButton = findViewById(R.id.convertOtherButton)

        convertToPlnButton.setOnClickListener { convertToPlnButtonClicked() }
        convertToOtherCurrencyButton.setOnClickListener { convertToOtherButtonClicked() }

        currenciesGetRequest(ApiHelper.CurrenciesTables.A)
        currenciesGetRequest(ApiHelper.CurrenciesTables.B)
    }

    private fun convertToOtherButtonClicked() {
        if (selectedCurrency == null) {
            ApiHelper.ToastError("No currency selected",this)
            return
        }

        val plnValue = plnEditTextNumber.text.toString().toDoubleOrNull() ?: 0.0

        val newValue = plnValue/selectedCurrency!!.Mid

        otherCurrencyEditTextNumber.setText(newValue.toString())
    }

    private fun convertToPlnButtonClicked() {
        if (selectedCurrency == null) {
            ApiHelper.ToastError("No currency selected",this)
            return
        }

        val otherValue = otherCurrencyEditTextNumber.text.toString().toDoubleOrNull() ?: 0.0

        val newValue = otherValue*selectedCurrency!!.Mid

        plnEditTextNumber.setText(newValue.toString())
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
            val currenciesCodes = currenciesList.map { currency -> currency.CurrencyCode }
            otherCurrencySpinner.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, currenciesCodes)
            if (otherCurrencySpinner.adapter.count > 0) {
                otherCurrencySpinner.setSelection(0)
            }
        } catch(e: Exception) {
            ApiHelper.ToastError(e.message, this)
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