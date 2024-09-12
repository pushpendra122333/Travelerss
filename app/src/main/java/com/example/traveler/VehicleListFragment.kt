package com.example.traveler


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater

class VehicleListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter
    private lateinit var selectedCategoryText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_list, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_vehicle_list)
        selectedCategoryText = view.findViewById(R.id.selected_category_text)
        recyclerView.layoutManager = LinearLayoutManager(context)

      val  category = arguments?.getString("category")
        // Display the selected category
        selectedCategoryText.text = "Category: $category"

        // Filter the car list based on the selected category
        val carList = getCarListByCategory(category)
        carAdapter = CarAdapter(carList)  {car ->
            val context = requireContext()
            val intent = Intent(context, BookingActivity::class.java)
            intent.putExtra("car", car)
            startActivity(intent)
        }
        recyclerView.adapter = carAdapter



        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    private fun getCarListByCategory(category: String?): List<Car> {
        val allCars = listOf(
            Car("Suzuki Swift Dzire", R.drawable.car1, 4, 2, 4, "₹ 3,000 / Per Day"),
            Car("Toyota Innova", R.drawable.carr2, 7, 2, 4, "₹ 5,000 / Per Day"),
            Car("Maruti Brezza", R.drawable.brezza1, 5, 2, 4, "₹ 4,000 / Per Day"),
            Car("HayaBusa", R.drawable.hayabusa, 2, 0, 0, "₹ 1,600 / Per Day"),
            Car("Mercedes", R.drawable.mercedes, 20, 8, 2, "₹ 12,000 / Per Day"),
            Car("Mercedes Mini Bus", R.drawable.mercedes2, 20, 8, 2, "₹ 10,000 / Per Day"),
            Car("TVS Ntorq 125", R.drawable.tvs, 2, 0, 0, "₹ 1,000 / Per Day"),
            Car("Yamaha Ray ZR 125 Fi Hybrid", R.drawable.yahama, 2, 0, 0, "₹ 1,100 / Per Day"),
            Car("Suzuki Access 125", R.drawable.access125, 2, 0, 0, "₹ 800 / Per Day"),
            Car("Suzuki Burgman Street", R.drawable.bugman, 2, 0, 0, "₹ 1,500 / Per Day"),
            Car("Honda Activa 6G", R.drawable.blueactiva, 2, 0, 0, "₹ 600 / Per Day"),
            Car("Yamaha Aerox 155", R.drawable.aerox, 2, 0, 0, "₹ 2,000 / Per Day"),
            Car("OLA S1 Pro", R.drawable.olascooty, 2, 0, 0, "₹ 2,200 / Per Day"),
            Car("Mahindra Suv", R.drawable.mahindra_suv, 10, 3, 5, "₹ 4,000 / Per Day"),
            Car("Renault Kwid", R.drawable.renault_kwd, 6, 2, 4, "₹ 4,000 / Per Day"),
            Car("Mahindra Thar", R.drawable.thar, 4, 2, 3, "₹ 4,000 / Per Day"),
            Car("Mahindra Scorpio", R.drawable.mahindra_scorpio, 10, 2, 5, "₹ 4,000 / Per Day"),
            Car("Royal Enfield Hunter 350", R.drawable.royal_hunter, 2, 0, 0, "₹ 2,500 / Per Day"),
            Car("Hero Splendor Plus", R.drawable.splendor, 2, 0, 0, "₹ 500 / Per Day"),
            Car("Yamaha R15 V4", R.drawable.yahama_r15, 2, 0, 0, "₹ 2,000 / Per Day"),
            Car("Royal Enfield Meteor 350", R.drawable.royal_metero, 2, 0, 0, "₹ 2,800 / Per Day"),
            Car("Honda Unicorn", R.drawable.honda_unicorn, 2, 0, 0, "₹ 700 / Per Day"),
            Car("Kawasaki Ninja H2R", R.drawable.kawasaki_ninja, 2, 0, 0, "₹ 2,600 / Per Day"),
            Car("Ford Transit", R.drawable.ford_minibus, 12, 4, 2, "₹ 8,000 / Per Day"),
            Car("Volkswagen Caravelle", R.drawable.volksvagon_minibus, 9, 4, 2, "₹ 9,000 / Per Day"),
            Car("Renault Master", R.drawable.renault_master, 8, 4, 2, "₹ 6,000 / Per Day"),
            Car("Renault Trafic", R.drawable.renault_minibus, 10, 4, 2, "₹ 7,200 / Per Day"),
            Car("Ford Tourneo Custom", R.drawable.ford_turneo, 8, 4, 4, "₹ 5,900 / Per Day")


        )

        return when (category) {
            "Bike" -> allCars.filter { it.name.contains("HayaBusa", true) || it.name.contains("Royal Enfield Hunter 350", true)|| it.name.contains("Hero Splendor Plus", true)|| it.name.contains("Yamaha R15 V4", true)|| it.name.contains("Royal Enfield Meteor 350", true)|| it.name.contains("Honda Unicorn", true)|| it.name.contains("Kawasaki Ninja H2R", true) }
            "Car" -> allCars.filter { it.name.contains("Swift Dzire", true) || it.name.contains("Toyota Innova", true) || it.name.contains("Brezza", true) || it.name.contains("Mahindra Suv", true) || it.name.contains("Renault Kwid", true)|| it.name.contains("Mahindra Thar", true)|| it.name.contains("Mahindra Scorpio", true) }
            "Minibus" -> allCars.filter { it.name.contains("Mercedes", true) || it.name.contains("Mercedes Mini Bus", true)|| it.name.contains("Ford Transit", true)|| it.name.contains("Volkswagen Caravelle", true)|| it.name.contains("Renault Master", true)|| it.name.contains("Renault Trafic", true)|| it.name.contains("Ford Tourneo Custom", true) }
            "Scooty" -> allCars.filter { it.name.contains("TVS Ntorq 125", true) || it.name.contains("Yamaha Ray ZR 125 Fi Hybrid", true) || it.name.contains("Suzuki Access 125", true) || it.name.contains("Suzuki Burgman Street", true) || it.name.contains("Honda Activa 6G", true) || it.name.contains("Yamaha Aerox 155", true) || it.name.contains("OLA S1 Pro", true)  }
            else -> emptyList()
        }
    }
}
