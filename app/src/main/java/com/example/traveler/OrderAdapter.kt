package com.example.traveler

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val bookingss: List<Booking>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vehicleName2: TextView = itemView.findViewById(R.id.vehicle_name1)
        val days2: TextView = itemView.findViewById(R.id.days1)
        val totalAmount2: TextView = itemView.findViewById(R.id.total_amount1)
        val bookingTime2: TextView = itemView.findViewById(R.id.booking_time1)
        val returnedStatus2: TextView = itemView.findViewById(R.id.returned_status1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val booking = bookingss[position]

        Log.d("OrderAdapter", "Binding view for position: $position with data: $booking")

        holder.vehicleName2.text = booking.vehicleName ?: "Unknown Vehicle"
        holder.days2.text = "Days: ${booking.days ?: 0}"
        holder.totalAmount2.text = "Total Amount: ${booking.totalAmount ?: "N/A"}"
        holder.bookingTime2.text = "Booking Time: ${booking.bookingTime ?: "N/A"}"
        holder.returnedStatus2.text = if (booking.returned) "Returned" else "Not Returned"
    }

    override fun getItemCount() = bookingss.size
}
