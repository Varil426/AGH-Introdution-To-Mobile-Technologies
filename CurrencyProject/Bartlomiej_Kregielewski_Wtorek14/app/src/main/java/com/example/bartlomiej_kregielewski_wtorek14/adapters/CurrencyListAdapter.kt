package com.example.bartlomiej_kregielewski_wtorek14.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bartlomiej_kregielewski_wtorek14.CurrencyDetailsActivity
import com.example.bartlomiej_kregielewski_wtorek14.FlagHelper
import com.example.bartlomiej_kregielewski_wtorek14.R
import com.example.bartlomiej_kregielewski_wtorek14.entities.Currency

class CurrencyListAdapter(private val context: Context, private val listOfCurrencies: List<Currency>) : RecyclerView.Adapter<CurrencyListAdapter.CurrencyView>() {

    class CurrencyView(val view: View) : RecyclerView.ViewHolder(view) {
        val codeTextView: TextView = itemView.findViewById(R.id.currencyCodeTextView)
        val midTextView: TextView = itemView.findViewById(R.id.midTextView)
        val flagTextView: TextView = itemView.findViewById(R.id.flagTextView)
        val riseImageView: ImageView = itemView.findViewById(R.id.riseImageView)
    }

    private fun loadCurrencyDetailsActivity(currency: Currency) {
        val intent = Intent(context, CurrencyDetailsActivity::class.java)
        intent.putExtra("Currency Code", currency.CurrencyCode)
        intent.putExtra("Table", currency.Table.toString())
        context.startActivity(intent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyView {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.currency_row, parent, false)
        return CurrencyView(view)
    }

    override fun onBindViewHolder(holder: CurrencyView, position: Int) {
        val currentCurrency = listOfCurrencies[position]
        holder.codeTextView.text = currentCurrency.CurrencyCode
        holder.midTextView.text = currentCurrency.Mid.toString()
        holder.flagTextView.text = FlagHelper.countryCodeToFlagEmoji(currentCurrency.CountryCode.toString())
        // When Rise == null don't set any image
        if (currentCurrency.Rise == true) {
            holder.riseImageView.setImageResource(R.drawable.green_arrow_up)
        } else if (currentCurrency.Rise == false) {
            holder.riseImageView.setImageResource(R.drawable.red_arrow_down)
        }
        holder.view.setOnClickListener { loadCurrencyDetailsActivity(currentCurrency) }
    }

    override fun getItemCount(): Int {
        return listOfCurrencies.size
    }

}