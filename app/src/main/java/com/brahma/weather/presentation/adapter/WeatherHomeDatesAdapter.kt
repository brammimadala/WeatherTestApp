package com.brahma.weather.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.brahma.weather.R
import com.brahma.weather.data.model.ForecastDataReportModel
import java.text.ParseException
import java.text.SimpleDateFormat

class WeatherHomeDatesAdapter(
    private val forecastWeatherReport: MutableList<ForecastDataReportModel>,
    private val listener: (ForecastDataReportModel) -> Unit
) : RecyclerView.Adapter<WeatherHomeDatesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_dates_cardstyle, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = forecastWeatherReport[position]
        holder.forecastDate.text = getDateWithFormat(item.dateKey)
        holder.layout.setOnClickListener { listener(item) }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return forecastWeatherReport.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val forecastDate: AppCompatTextView = itemView.findViewById(R.id.forecast_date)
        val layout: View = itemView.findViewById(R.id.dateLayout)
    }

    fun getDateWithFormat(date: String?): String? {
        return try {
            val dateFrmt = SimpleDateFormat("yyyy-MM-dd")
            val dateFrmt1 = SimpleDateFormat("E\ndd\nMMM")
            val d1 = dateFrmt.parse(date)
            dateFrmt1.format(d1)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}