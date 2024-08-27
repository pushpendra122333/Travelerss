package com.example.traveler


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VehicleListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_vehicle_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

      val  category = arguments?.getString("category")

        // Filter the car list based on the selected category
        val carList = getCarListByCategory(category)
        carAdapter = CarAdapter(carList)  {car ->
            val context = requireContext()
            val intent = Intent(context, BookingActivity::class.java)
            intent.putExtra("car", car)
            startActivity(intent)
        }
        recyclerView.adapter = carAdapter
    }

    private fun getCarListByCategory(category: String?): List<Car> {
        val allCars = listOf(
            Car("Suzuki Swift Dzire", R.drawable.car, 4, 2, 4, "₹ 3,000 / Per Day"),
            Car("Toyota Innova", R.drawable.car2, 7, 2, 4, "₹ 5,000 / Per Day"),
            Car("Maruti Baleno", R.drawable.baleno, 5, 2, 4, "₹ 4,000 / Per Day"),
            Car("Maruti Brezza", R.drawable.brezza, 5, 2, 4, "₹ 4,000 / Per Day"),
            Car("HayaBusa", R.drawable.hayabusa, 2, 0, 0, "₹ 2,000 / Per Day"),
            Car("Mercedes", R.drawable.mercedes, 20, 8, 2, "₹ 12,000 / Per Day"),
            Car("Mercedes Mini Bus", R.drawable.mercedes2, 20, 8, 2, "₹ 10,000 / Per Day")
        )

        return when (category) {
            "Bike" -> allCars.filter { it.name.contains("HayaBusa", true) }
            "Car" -> allCars.filter { it.name.contains("Swift Dzire", true) || it.name.contains("Innova", true) || it.name.contains("Baleno", true) || it.name.contains("Brezza", true) }
            "Minibus" -> allCars.filter { it.name.contains("Mercedes", true) || it.name.contains("Mercedes Mini Bus", true) }
            else -> emptyList()
        }
    }
}
