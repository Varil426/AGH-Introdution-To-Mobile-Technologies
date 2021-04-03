package com.example.bartlomiej_kregielewski_wtorek14

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlin.system.exitProcess

// Bartłomiej Kręgielewski Wtorek 14:00
// What was done? - Everything
// 1. Done
// 2. Done
// 3. Done - currencies list has data from tables A and B.
// 4. Done
// 4.a Done
// 4.b Done - emoji flags, for missing there is thinking emoji, for multi-country currencies there is special mapping.
// 4.c Done - as stated above.
// 4.d Done - there is a button that let's you change data from 7 to 30 days. X axis has dates as labels.
// I hope it's done as it's supposed to be done because the task's description uses dates and latest data interchangeable, which can be confusing for table B (data is updated once per week) and for situations like app is opened just after midnight.
// 4.e Done
// 5. Done
// 6. Done

class MainActivity : AppCompatActivity() {

    lateinit var currenciesButton: Button
    lateinit var goldButton: Button
    lateinit var exchangeCalculatorButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CheckInternetState()

        currenciesButton = findViewById(R.id.currenciesButton)
        goldButton = findViewById(R.id.goldButton)
        exchangeCalculatorButton = findViewById(R.id.exchangeCalculatorButton)

        currenciesButton.setOnClickListener { OpenCurrenciesTable() }
        goldButton.setOnClickListener { OpenGoldDetails() }
        exchangeCalculatorButton.setOnClickListener { OpenExchangeRatesCalculator() }
    }

    private fun CheckInternetState() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (!(connectivityManager.activeNetworkInfo?.isConnected ?: false)) {
            ApiHelper.ToastError("No network connection.", this)
        }
    }

    private fun OpenCurrenciesTable() {
        val intent = Intent(this, CurrenciesTableActivity::class.java)
        startActivity(intent)
    }

    private fun OpenGoldDetails() {
        val intent = Intent(this, GoldDetailsActivity::class.java)
        startActivity(intent)
    }

    private fun OpenExchangeRatesCalculator() {
        val intent = Intent(this, ExchangeRateCalculatorActivity::class.java)
        startActivity(intent)
    }
}