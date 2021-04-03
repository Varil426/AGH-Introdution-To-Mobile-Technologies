package com.example.bartlomiej_kregielewski_wtorek14.entities

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.example.bartlomiej_kregielewski_wtorek14.ApiHelper
import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.CountryCode.*

class Currency(name: String, code: String, mid: Double, table: ApiHelper.CurrenciesTables) {

    companion object {
        private val specialMappingCurrencies = mapOf("USD" to US, "GBP" to GB, "EUR" to EU, "HKD" to HK, "CHF" to SE)
    }

    val Name = name
    val CurrencyCode = code
    val Mid = mid
    val Table = table
    var CountryCode: CountryCode?
        private set


    var Rise: Boolean? = null

    init {
        CountryCode = if (CurrencyCode in specialMappingCurrencies) {
            specialMappingCurrencies[CurrencyCode]
        } else {
            values().firstOrNull { x -> x.currency?.currencyCode == CurrencyCode }
        }
    }
}