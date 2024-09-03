package com.example.traveler

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class HomePage : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter
    private lateinit var searchBar: EditText
    private lateinit var filterSpinner: Spinner
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        searchBar = view.findViewById(R.id.search_bar)
        filterSpinner = view.findViewById(R.id.filter_spinner)
        toolbar = view.findViewById(R.id.toolbar)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        recyclerView.layoutManager = LinearLayoutManager(context)


        val carList = listOf(
            Car("Suzuki Swift Dzire", R.drawable.car1, 4, 2, 4, "₹ 3,000 / Per Day"),
            Car("Toyota Innova", R.drawable.carr2, 7, 2, 4, "₹ 5,000 / Per Day"),
            Car("Maruti Baleno", R.drawable.baleno1, 5, 2, 4, "₹ 4,000 / Per Day"),
            Car("Maruti Brezza", R.drawable.brezza1, 5, 2, 4, "₹ 4,000 / Per Day"),
            Car("HayaBusa", R.drawable.hayabusa, 2, 0, 0, "₹ 2,000 / Per Day"),
            Car("Mercedes", R.drawable.mercedes, 20, 8, 2, "₹ 12,000 / Per Day"),
            Car("Mercedes Mini Bus", R.drawable.mercedes2, 20, 8, 2, "₹ 10,000 / Per Day")


        )

        carAdapter = CarAdapter(carList) { car ->
            val context = requireContext()
            val intent = Intent(context, BookingActivity::class.java)
            intent.putExtra("car", car)

            startActivity(intent)
        }
        recyclerView.adapter = carAdapter



        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                carAdapter.filter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val filterOptions = arrayOf("Default", "Price: Low to High", "Price: High to Low", "Min. 2 Passengers", "Min. 4 Passengers", "Min. 6 Passengers")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, filterOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = adapter

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> carAdapter.resetData() // Default
                    1 -> carAdapter.sortByPriceAscending()
                    2 -> carAdapter.sortByPriceDescending()
                    3 -> carAdapter.filterByPassengerCapacity(2)
                    4 -> carAdapter.filterByPassengerCapacity(4)
                    5 -> carAdapter.filterByPassengerCapacity(6)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home_page, menu)

        val feedbackItem = menu.findItem(R.id.action_feedback)
        val helpItem = menu.findItem(R.id.action_help)

        // Set the custom color for each menu item
        setMenuItemTextColor(feedbackItem, R.color.menu_item_text_color)
        setMenuItemTextColor(helpItem, R.color.menu_item_text_color)

        super.onCreateOptionsMenu(menu, inflater)

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_feedback -> {
                // Handle feedback action

               val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://alike-jam-bee.notion.site/FEEDBACK-2ddf172a2229471aaf839df4c2968520?pvs=4")

                startActivity(i)
                true
            }
            R.id.action_help -> {
                // Handle help action

                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:ps3389401@gmail.com")
                }
                startActivity(Intent.createChooser(emailIntent, "Send email"))

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun setMenuItemTextColor(menuItem: MenuItem, colorResId: Int) {
        val spanString = SpannableString(menuItem.title)
        spanString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), colorResId)),
            0,
            spanString.length,
            0
        )
        menuItem.title = spanString
    }


}