package com.brahma.weather.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.brahma.weather.R
import com.brahma.weather.data.model.ForeCastTimeModel
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat

class ForecastWeatherDetailAdapter(
    private val context: Context,
    private val forecastWeatherReport: MutableList<ForeCastTimeModel>,
    private val listener: (ForeCastTimeModel) -> Unit
) : RecyclerView.Adapter<ForecastWeatherDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_time_cardstyle, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val fcr: ForeCastTimeModel = forecastWeatherReport[position]
        val fcrTime = fcr.FcData.dtTxt
        holder.forecastTime.text = getDateWithFormat(fcrTime)
        holder.weatherReportTemperature.text = "${fcr.FcData.main.temp}°"

        if (fcr.FcData.weather.get(0).icon != null) {
            val imageUrl: String =
                "https://api.openweathermap.org/img/w/${fcr.FcData.weather[0].icon}"
            Glide.with(context)
                .load(imageUrl)
                .into(holder.weatherReportImage)
        }

        holder.layout.setOnClickListener { listener(fcr) }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return forecastWeatherReport.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val forecastTime: AppCompatTextView = itemView.findViewById(R.id.forecast_time)
        val weatherReportTemperature: AppCompatTextView =
            itemView.findViewById(R.id.weatherReport_temperature)
        val weatherReportImage: AppCompatImageView = itemView.findViewById(R.id.weatherReport_image)
        val layout: View = itemView.findViewById(R.id.dateLayout)
    }

    fun getDateWithFormat(date: String?): String? {
        return try {
            val dateFrmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateFrmt1 = SimpleDateFormat("hh:mm a")
            val d1 = dateFrmt.parse(date)
            dateFrmt1.format(d1)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}