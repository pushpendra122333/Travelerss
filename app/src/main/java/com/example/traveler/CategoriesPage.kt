package com.example.traveler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriesPage : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_category)
        recyclerView.layoutManager = LinearLayoutManager(context)


        // Set space between items (e.g., 16dp)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_spacing)
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(spacingInPixels))

        // Define the categories
        val categories = listOf("Bike", "Car", "Minibus")

        // Set up the adapter and handle clicks
        categoryAdapter = CategoryAdapter(categories) { category ->
            // Navigate to the VehicleListFragment with the selected category
            val fragment = VehicleListFragment()
            val bundle = Bundle()
            bundle.putString("category", category)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = categoryAdapter
    }
}