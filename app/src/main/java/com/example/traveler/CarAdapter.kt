package com.example.traveler

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(private var carList: List<Car>,private val onBookClick: (Car) -> Unit) : RecyclerView.Adapter<CarAdapter.CarViewHolder>(), Filterable {

    private var carListFiltered: List<Car> = carList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carListFiltered[position]
        holder.bind(car)

        // Handle the "Book" button click
        holder.bookButton.setOnClickListener {

            onBookClick(car)
        }
        holder.carImage.setOnClickListener{
            onBookClick(car)
        }
    }

    override fun getItemCount(): Int = carListFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    carList
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    carList.filter {
                        it.name.lowercase().contains(filterPattern)
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                carListFiltered = results?.values as List<Car>
                notifyDataSetChanged()
            }
        }
    }

    fun sortByPriceAscending() {
        carListFiltered = carListFiltered.sortedBy { it.price.toPrice() }
        notifyDataSetChanged()
    }

    fun sortByPriceDescending() {
        carListFiltered = carListFiltered.sortedByDescending { it.price.toPrice() }
        notifyDataSetChanged()
    }

    fun filterByPassengerCapacity(minCapacity: Int) {
        carListFiltered = carList.filter { it.passengerCapacity >= minCapacity }
        notifyDataSetChanged()
    }

    fun resetData() {
        carListFiltered = carList
        notifyDataSetChanged()
    }

    private fun String.toPrice(): Int {
        return this.replace("₹", "").replace(",", "").trim().split(" ")[0].toIntOrNull() ?: 0
    }

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val carImage: ImageView = itemView.findViewById(R.id.car_image)
        private val carName: TextView = itemView.findViewById(R.id.car_name)
        private val carPassenger: TextView = itemView.findViewById(R.id.car_passenger)
        private val carBaggage: TextView = itemView.findViewById(R.id.car_baggage)
        private val carDoors: TextView = itemView.findViewById(R.id.car_doors)
        private val carPrice: TextView = itemView.findViewById(R.id.car_price)
        public val bookButton: TextView = itemView.findViewById(R.id.book_button)

        fun bind(car: Car) {
            carImage.setImageResource(car.imageResource)
            carName.text = car.name
            carPassenger.text = car.passengerCapacity.toString()
            carBaggage.text = car.baggageCapacity.toString()
            carDoors.text = car.numberOfDoors.toString()
            carPrice.text = car.price

            // Optional: Add functionality for the book button if needed
            bookButton.setOnClickListener {
                // Handle book button click
            }
        }
    }
}