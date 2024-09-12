package com.example.traveler

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private var bookingss: List<Booking>,
                   private var onCancelClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vehicleName2: TextView = itemView.findViewById(R.id.vehicle_name1)
        val days2: TextView = itemView.findViewById(R.id.days1)
        val totalAmount2: TextView = itemView.findViewById(R.id.total_amount1)
        val bookingTime2: TextView = itemView.findViewById(R.id.booking_time1)
        val startdates: TextView = itemView.findViewById(R.id.startdates)
        val enddates: TextView = itemView.findViewById(R.id.enddates)
        val useraddress: TextView = itemView.findViewById(R.id.useraddress)
        val returnedStatus2: TextView = itemView.findViewById(R.id.returned_status1)
        val cancelButton: Button = itemView.findViewById(R.id.button_cancel)

        fun setOnCancelClickListener(listener: (Int) -> Unit) {
            onCancelClickListener = listener
        }

        init {
            cancelButton.setOnClickListener {
                val booking = bookingss[adapterPosition]
                if (!booking.returned && !booking.canceled) {
                    onCancelClickListener?.invoke(booking.bookingId)
                }
            }
        }
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
        holder.startdates.text = "Pick Up Date: ${booking.startdate ?: "N/A"}"
        holder.enddates.text = "Return Date: ${booking.enddate ?: "N/A"}"
        holder.useraddress.text = "UserAddress: ${booking.useraddress ?: "N/A"}"
        holder.returnedStatus2.text = if (booking.returned) "Returned" else "Not Returned"

        if (booking.returned) {
            holder.returnedStatus2.text = "Returned"
        } else {
            holder.returnedStatus2.text = "Not Returned"
        }

        if (booking.returned) {
            holder.returnedStatus2.text = "Returned"
            holder.cancelButton.visibility = View.GONE
        } else if (booking.canceled) {
            holder.returnedStatus2.text = "Canceled. Charge: ${booking.cancellationCharge}"
            holder.cancelButton.visibility = View.GONE
        } else {
            holder.returnedStatus2.text = "Not Returned"
            holder.cancelButton.visibility = View.VISIBLE
        }
    }
    fun updateBookings(newBookings: List<Booking>) {
        bookingss = newBookings
        notifyDataSetChanged()
    }


    override fun getItemCount() = bookingss.size
}
